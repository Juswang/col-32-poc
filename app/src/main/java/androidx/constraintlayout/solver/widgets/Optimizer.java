package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;

/* loaded from: classes2.dex */
public class Optimizer {
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    static final int FLAG_USE_OPTIMIZE = 0;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_GROUPS = 32;
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_STANDARD = 7;
    static boolean[] flags = new boolean[3];

    public static void checkMatchParent(ConstraintWidgetContainer container, LinearSystem system, ConstraintWidget widget) {
        if (container.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int left = widget.mLeft.mMargin;
            int right = container.getWidth() - widget.mRight.mMargin;
            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
            system.addEquality(widget.mLeft.mSolverVariable, left);
            system.addEquality(widget.mRight.mSolverVariable, right);
            widget.mHorizontalResolution = 2;
            widget.setHorizontalDimension(left, right);
        }
        if (container.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int top = widget.mTop.mMargin;
            int bottom = container.getHeight() - widget.mBottom.mMargin;
            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
            system.addEquality(widget.mTop.mSolverVariable, top);
            system.addEquality(widget.mBottom.mSolverVariable, bottom);
            if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline);
                system.addEquality(widget.mBaseline.mSolverVariable, widget.mBaselineDistance + top);
            }
            widget.mVerticalResolution = 2;
            widget.setVerticalDimension(top, bottom);
        }
    }

    private static boolean optimizableMatchConstraint(ConstraintWidget constraintWidget, int orientation) {
        if (constraintWidget.mListDimensionBehaviors[orientation] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        char c = 1;
        if (constraintWidget.mDimensionRatio != 0.0f) {
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.mListDimensionBehaviors;
            if (orientation != 0) {
                c = 0;
            }
            return dimensionBehaviourArr[c] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? false : false;
        }
        if (orientation == 0) {
            if (constraintWidget.mMatchConstraintDefaultWidth != 0 || constraintWidget.mMatchConstraintMinWidth != 0 || constraintWidget.mMatchConstraintMaxWidth != 0) {
                return false;
            }
        } else if (constraintWidget.mMatchConstraintDefaultHeight != 0 || constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
            return false;
        }
        return true;
    }

    public static void analyze(int optimisationLevel, ConstraintWidget widget) {
        widget.updateResolutionNodes();
        ResolutionAnchor leftNode = widget.mLeft.getResolutionNode();
        ResolutionAnchor topNode = widget.mTop.getResolutionNode();
        ResolutionAnchor rightNode = widget.mRight.getResolutionNode();
        ResolutionAnchor bottomNode = widget.mBottom.getResolutionNode();
        boolean optimiseDimensions = (optimisationLevel & 8) == 8;
        boolean isOptimizableHorizontalMatch = widget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(widget, 0);
        if (!(leftNode.type == 4 || rightNode.type == 4)) {
            if (widget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || (isOptimizableHorizontalMatch && widget.getVisibility() == 8)) {
                if (widget.mLeft.mTarget == null && widget.mRight.mTarget == null) {
                    leftNode.setType(1);
                    rightNode.setType(1);
                    if (optimiseDimensions) {
                        rightNode.dependsOn(leftNode, 1, widget.getResolutionWidth());
                    } else {
                        rightNode.dependsOn(leftNode, widget.getWidth());
                    }
                } else if (widget.mLeft.mTarget != null && widget.mRight.mTarget == null) {
                    leftNode.setType(1);
                    rightNode.setType(1);
                    if (optimiseDimensions) {
                        rightNode.dependsOn(leftNode, 1, widget.getResolutionWidth());
                    } else {
                        rightNode.dependsOn(leftNode, widget.getWidth());
                    }
                } else if (widget.mLeft.mTarget == null && widget.mRight.mTarget != null) {
                    leftNode.setType(1);
                    rightNode.setType(1);
                    leftNode.dependsOn(rightNode, -widget.getWidth());
                    if (optimiseDimensions) {
                        leftNode.dependsOn(rightNode, -1, widget.getResolutionWidth());
                    } else {
                        leftNode.dependsOn(rightNode, -widget.getWidth());
                    }
                } else if (!(widget.mLeft.mTarget == null || widget.mRight.mTarget == null)) {
                    leftNode.setType(2);
                    rightNode.setType(2);
                    if (optimiseDimensions) {
                        widget.getResolutionWidth().addDependent(leftNode);
                        widget.getResolutionWidth().addDependent(rightNode);
                        leftNode.setOpposite(rightNode, -1, widget.getResolutionWidth());
                        rightNode.setOpposite(leftNode, 1, widget.getResolutionWidth());
                    } else {
                        leftNode.setOpposite(rightNode, -widget.getWidth());
                        rightNode.setOpposite(leftNode, widget.getWidth());
                    }
                }
            } else if (isOptimizableHorizontalMatch) {
                int width = widget.getWidth();
                leftNode.setType(1);
                rightNode.setType(1);
                if (widget.mLeft.mTarget == null && widget.mRight.mTarget == null) {
                    if (optimiseDimensions) {
                        rightNode.dependsOn(leftNode, 1, widget.getResolutionWidth());
                    } else {
                        rightNode.dependsOn(leftNode, width);
                    }
                } else if (widget.mLeft.mTarget == null || widget.mRight.mTarget != null) {
                    if (widget.mLeft.mTarget != null || widget.mRight.mTarget == null) {
                        if (!(widget.mLeft.mTarget == null || widget.mRight.mTarget == null)) {
                            if (optimiseDimensions) {
                                widget.getResolutionWidth().addDependent(leftNode);
                                widget.getResolutionWidth().addDependent(rightNode);
                            }
                            if (widget.mDimensionRatio == 0.0f) {
                                leftNode.setType(3);
                                rightNode.setType(3);
                                leftNode.setOpposite(rightNode, 0.0f);
                                rightNode.setOpposite(leftNode, 0.0f);
                            } else {
                                leftNode.setType(2);
                                rightNode.setType(2);
                                leftNode.setOpposite(rightNode, -width);
                                rightNode.setOpposite(leftNode, width);
                                widget.setWidth(width);
                            }
                        }
                    } else if (optimiseDimensions) {
                        leftNode.dependsOn(rightNode, -1, widget.getResolutionWidth());
                    } else {
                        leftNode.dependsOn(rightNode, -width);
                    }
                } else if (optimiseDimensions) {
                    rightNode.dependsOn(leftNode, 1, widget.getResolutionWidth());
                } else {
                    rightNode.dependsOn(leftNode, width);
                }
            }
        }
        boolean isOptimizableVerticalMatch = widget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(widget, 1);
        if (topNode.type != 4 && bottomNode.type != 4) {
            if (widget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || (isOptimizableVerticalMatch && widget.getVisibility() == 8)) {
                if (widget.mTop.mTarget == null && widget.mBottom.mTarget == null) {
                    topNode.setType(1);
                    bottomNode.setType(1);
                    if (optimiseDimensions) {
                        bottomNode.dependsOn(topNode, 1, widget.getResolutionHeight());
                    } else {
                        bottomNode.dependsOn(topNode, widget.getHeight());
                    }
                    if (widget.mBaseline.mTarget != null) {
                        widget.mBaseline.getResolutionNode().setType(1);
                        topNode.dependsOn(1, widget.mBaseline.getResolutionNode(), -widget.mBaselineDistance);
                    }
                } else if (widget.mTop.mTarget != null && widget.mBottom.mTarget == null) {
                    topNode.setType(1);
                    bottomNode.setType(1);
                    if (optimiseDimensions) {
                        bottomNode.dependsOn(topNode, 1, widget.getResolutionHeight());
                    } else {
                        bottomNode.dependsOn(topNode, widget.getHeight());
                    }
                    if (widget.mBaselineDistance > 0) {
                        widget.mBaseline.getResolutionNode().dependsOn(1, topNode, widget.mBaselineDistance);
                    }
                } else if (widget.mTop.mTarget == null && widget.mBottom.mTarget != null) {
                    topNode.setType(1);
                    bottomNode.setType(1);
                    if (optimiseDimensions) {
                        topNode.dependsOn(bottomNode, -1, widget.getResolutionHeight());
                    } else {
                        topNode.dependsOn(bottomNode, -widget.getHeight());
                    }
                    if (widget.mBaselineDistance > 0) {
                        widget.mBaseline.getResolutionNode().dependsOn(1, topNode, widget.mBaselineDistance);
                    }
                } else if (widget.mTop.mTarget != null && widget.mBottom.mTarget != null) {
                    topNode.setType(2);
                    bottomNode.setType(2);
                    if (optimiseDimensions) {
                        topNode.setOpposite(bottomNode, -1, widget.getResolutionHeight());
                        bottomNode.setOpposite(topNode, 1, widget.getResolutionHeight());
                        widget.getResolutionHeight().addDependent(topNode);
                        widget.getResolutionWidth().addDependent(bottomNode);
                    } else {
                        topNode.setOpposite(bottomNode, -widget.getHeight());
                        bottomNode.setOpposite(topNode, widget.getHeight());
                    }
                    if (widget.mBaselineDistance > 0) {
                        widget.mBaseline.getResolutionNode().dependsOn(1, topNode, widget.mBaselineDistance);
                    }
                }
            } else if (isOptimizableVerticalMatch) {
                int height = widget.getHeight();
                topNode.setType(1);
                bottomNode.setType(1);
                if (widget.mTop.mTarget == null && widget.mBottom.mTarget == null) {
                    if (optimiseDimensions) {
                        bottomNode.dependsOn(topNode, 1, widget.getResolutionHeight());
                    } else {
                        bottomNode.dependsOn(topNode, height);
                    }
                } else if (widget.mTop.mTarget == null || widget.mBottom.mTarget != null) {
                    if (widget.mTop.mTarget != null || widget.mBottom.mTarget == null) {
                        if (widget.mTop.mTarget != null && widget.mBottom.mTarget != null) {
                            if (optimiseDimensions) {
                                widget.getResolutionHeight().addDependent(topNode);
                                widget.getResolutionWidth().addDependent(bottomNode);
                            }
                            if (widget.mDimensionRatio == 0.0f) {
                                topNode.setType(3);
                                bottomNode.setType(3);
                                topNode.setOpposite(bottomNode, 0.0f);
                                bottomNode.setOpposite(topNode, 0.0f);
                                return;
                            }
                            topNode.setType(2);
                            bottomNode.setType(2);
                            topNode.setOpposite(bottomNode, -height);
                            bottomNode.setOpposite(topNode, height);
                            widget.setHeight(height);
                            if (widget.mBaselineDistance > 0) {
                                widget.mBaseline.getResolutionNode().dependsOn(1, topNode, widget.mBaselineDistance);
                            }
                        }
                    } else if (optimiseDimensions) {
                        topNode.dependsOn(bottomNode, -1, widget.getResolutionHeight());
                    } else {
                        topNode.dependsOn(bottomNode, -height);
                    }
                } else if (optimiseDimensions) {
                    bottomNode.dependsOn(topNode, 1, widget.getResolutionHeight());
                } else {
                    bottomNode.dependsOn(topNode, height);
                }
            }
        }
    }

    public static boolean applyChainOptimized(ConstraintWidgetContainer container, LinearSystem system, int orientation, int offset, ChainHead chainHead) {
        boolean isChainSpreadInside;
        boolean isChainSpread;
        boolean isChainPacked;
        float distance;
        float distance2;
        ConstraintWidget widget;
        float dimension;
        float distance3;
        float dimension2;
        float extraMargin;
        ResolutionAnchor lastNode;
        int numMatchConstraints;
        int numMatchConstraints2;
        ConstraintWidget next;
        int i = orientation;
        ConstraintWidget first = chainHead.mFirst;
        ConstraintWidget last = chainHead.mLast;
        ConstraintWidget firstVisibleWidget = chainHead.mFirstVisibleWidget;
        ConstraintWidget lastVisibleWidget = chainHead.mLastVisibleWidget;
        ConstraintWidget head = chainHead.mHead;
        boolean done = false;
        float totalWeights = chainHead.mTotalWeight;
        ConstraintWidget firstMatchConstraintsWidget = chainHead.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget = chainHead.mLastMatchConstraintWidget;
        boolean isWrapContent = container.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            isChainSpread = head.mHorizontalChainStyle == 0;
            isChainSpreadInside = head.mHorizontalChainStyle == 1;
            isChainPacked = head.mHorizontalChainStyle == 2;
        } else {
            isChainSpread = head.mVerticalChainStyle == 0;
            isChainSpreadInside = head.mVerticalChainStyle == 1;
            isChainPacked = head.mVerticalChainStyle == 2;
        }
        int numMatchConstraints3 = 0;
        int numVisibleWidgets = 0;
        float totalMargins = 0.0f;
        float totalSize = 0.0f;
        ConstraintWidget widget2 = first;
        while (true) {
            ConstraintWidget firstMatchConstraintsWidget2 = firstMatchConstraintsWidget;
            if (!done) {
                boolean done2 = done;
                if (widget2.getVisibility() != 8) {
                    numVisibleWidgets++;
                    if (i == 0) {
                        totalSize += widget2.getWidth();
                    } else {
                        totalSize += widget2.getHeight();
                    }
                    if (widget2 != firstVisibleWidget) {
                        totalSize += widget2.mListAnchors[offset].getMargin();
                    }
                    if (widget2 != lastVisibleWidget) {
                        totalSize += widget2.mListAnchors[offset + 1].getMargin();
                    }
                    totalMargins = totalMargins + widget2.mListAnchors[offset].getMargin() + widget2.mListAnchors[offset + 1].getMargin();
                }
                ConstraintAnchor constraintAnchor = widget2.mListAnchors[offset];
                if (widget2.getVisibility() != 8 && widget2.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    numMatchConstraints3++;
                    if (i != 0) {
                        if (!(widget2.mMatchConstraintDefaultHeight == 0 && widget2.mMatchConstraintMinHeight == 0 && widget2.mMatchConstraintMaxHeight == 0)) {
                            break;
                        }
                    } else if (!(widget2.mMatchConstraintDefaultWidth == 0 && widget2.mMatchConstraintMinWidth == 0 && widget2.mMatchConstraintMaxWidth == 0)) {
                        break;
                    }
                    if (widget2.mDimensionRatio != 0.0f) {
                        return false;
                    }
                }
                ConstraintAnchor nextAnchor = widget2.mListAnchors[offset + 1].mTarget;
                if (nextAnchor != null) {
                    ConstraintWidget next2 = nextAnchor.mOwner;
                    numMatchConstraints2 = numMatchConstraints3;
                    next = (next2.mListAnchors[offset].mTarget == null || next2.mListAnchors[offset].mTarget.mOwner != widget2) ? null : next2;
                } else {
                    numMatchConstraints2 = numMatchConstraints3;
                    next = null;
                }
                if (next != null) {
                    widget2 = next;
                    done = done2;
                } else {
                    done = true;
                }
                numMatchConstraints3 = numMatchConstraints2;
                firstMatchConstraintsWidget = firstMatchConstraintsWidget2;
            } else {
                ResolutionAnchor firstNode = first.mListAnchors[offset].getResolutionNode();
                ResolutionAnchor lastNode2 = last.mListAnchors[offset + 1].getResolutionNode();
                if (firstNode.target != null && lastNode2.target != null && firstNode.target.state == 1 && lastNode2.target.state == 1) {
                    if (numMatchConstraints3 > 0 && numMatchConstraints3 != numVisibleWidgets) {
                        return false;
                    }
                    float extraMargin2 = 0.0f;
                    if (isChainPacked || isChainSpread || isChainSpreadInside) {
                        if (firstVisibleWidget != null) {
                            extraMargin2 = firstVisibleWidget.mListAnchors[offset].getMargin();
                        }
                        if (lastVisibleWidget != null) {
                            extraMargin2 += lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                        }
                    }
                    float firstOffset = firstNode.target.resolvedOffset;
                    boolean isChainPacked2 = isChainPacked;
                    float lastOffset = lastNode2.target.resolvedOffset;
                    if (firstOffset < lastOffset) {
                        distance = (lastOffset - firstOffset) - totalSize;
                    } else {
                        distance = (firstOffset - lastOffset) - totalSize;
                    }
                    if (numMatchConstraints3 <= 0 || numMatchConstraints3 != numVisibleWidgets) {
                        float extraMargin3 = extraMargin2;
                        if (distance < 0.0f) {
                            isChainSpread = false;
                            isChainSpreadInside = false;
                            isChainPacked2 = true;
                        }
                        if (isChainPacked2) {
                            float distance4 = (first.getBiasPercent(i) * (distance - extraMargin3)) + firstOffset;
                            ConstraintWidget widget3 = first;
                            while (widget3 != null) {
                                if (LinearSystem.sMetrics != null) {
                                    LinearSystem.sMetrics.nonresolvedWidgets--;
                                    LinearSystem.sMetrics.resolvedWidgets++;
                                    LinearSystem.sMetrics.chainConnectionResolved++;
                                }
                                ConstraintWidget next3 = widget3.mNextChainWidget[i];
                                if (next3 != null || widget3 == last) {
                                    if (i == 0) {
                                        dimension2 = widget3.getWidth();
                                    } else {
                                        dimension2 = widget3.getHeight();
                                    }
                                    float distance5 = distance4 + widget3.mListAnchors[offset].getMargin();
                                    widget3.mListAnchors[offset].getResolutionNode().resolve(firstNode.resolvedTarget, distance5);
                                    widget3.mListAnchors[offset + 1].getResolutionNode().resolve(firstNode.resolvedTarget, distance5 + dimension2);
                                    widget3.mListAnchors[offset].getResolutionNode().addResolvedValue(system);
                                    widget3.mListAnchors[offset + 1].getResolutionNode().addResolvedValue(system);
                                    distance4 = distance5 + dimension2 + widget3.mListAnchors[offset + 1].getMargin();
                                }
                                widget3 = next3;
                            }
                            return true;
                        } else if (!isChainSpread && !isChainSpreadInside) {
                            return true;
                        } else {
                            if (isChainSpread) {
                                distance -= extraMargin3;
                            } else if (isChainSpreadInside) {
                                distance -= extraMargin3;
                            }
                            float gap = distance / (numVisibleWidgets + 1);
                            if (isChainSpreadInside) {
                                if (numVisibleWidgets > 1) {
                                    gap = distance / (numVisibleWidgets - 1);
                                } else {
                                    gap = distance / 2.0f;
                                }
                            }
                            float distance6 = firstOffset;
                            if (first.getVisibility() != 8) {
                                distance6 += gap;
                            }
                            if (isChainSpreadInside && numVisibleWidgets > 1) {
                                distance6 = firstOffset + firstVisibleWidget.mListAnchors[offset].getMargin();
                            }
                            if (!isChainSpread || firstVisibleWidget == null) {
                                widget = first;
                                distance2 = distance6;
                            } else {
                                widget = first;
                                distance2 = distance6 + firstVisibleWidget.mListAnchors[offset].getMargin();
                            }
                            while (widget != null) {
                                if (LinearSystem.sMetrics != null) {
                                    LinearSystem.sMetrics.nonresolvedWidgets--;
                                    LinearSystem.sMetrics.resolvedWidgets++;
                                    LinearSystem.sMetrics.chainConnectionResolved++;
                                }
                                ConstraintWidget next4 = widget.mNextChainWidget[i];
                                if (next4 != null || widget == last) {
                                    if (i == 0) {
                                        dimension = widget.getWidth();
                                    } else {
                                        dimension = widget.getHeight();
                                    }
                                    if (widget != firstVisibleWidget) {
                                        distance3 = distance2 + widget.mListAnchors[offset].getMargin();
                                    } else {
                                        distance3 = distance2;
                                    }
                                    widget.mListAnchors[offset].getResolutionNode().resolve(firstNode.resolvedTarget, distance3);
                                    widget.mListAnchors[offset + 1].getResolutionNode().resolve(firstNode.resolvedTarget, distance3 + dimension);
                                    widget.mListAnchors[offset].getResolutionNode().addResolvedValue(system);
                                    widget.mListAnchors[offset + 1].getResolutionNode().addResolvedValue(system);
                                    distance2 = distance3 + widget.mListAnchors[offset + 1].getMargin() + dimension;
                                    if (!(next4 == null || next4.getVisibility() == 8)) {
                                        distance2 += gap;
                                    }
                                }
                                widget = next4;
                                i = orientation;
                            }
                            return true;
                        }
                    } else {
                        if (widget2.getParent() != null && widget2.getParent().mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                            return false;
                        }
                        float distance7 = (distance + totalSize) - totalMargins;
                        ConstraintWidget widget4 = first;
                        float position = firstOffset;
                        while (widget4 != null) {
                            if (LinearSystem.sMetrics != null) {
                                lastNode = lastNode2;
                                extraMargin = extraMargin2;
                                LinearSystem.sMetrics.nonresolvedWidgets--;
                                LinearSystem.sMetrics.resolvedWidgets++;
                                LinearSystem.sMetrics.chainConnectionResolved++;
                            } else {
                                lastNode = lastNode2;
                                extraMargin = extraMargin2;
                            }
                            ConstraintWidget next5 = widget4.mNextChainWidget[i];
                            if (next5 != null || widget4 == last) {
                                float dimension3 = distance7 / numMatchConstraints3;
                                if (totalWeights > 0.0f) {
                                    if (widget4.mWeight[i] == -1.0f) {
                                        dimension3 = 0.0f;
                                    } else {
                                        dimension3 = (widget4.mWeight[i] * distance7) / totalWeights;
                                    }
                                }
                                if (widget4.getVisibility() == 8) {
                                    dimension3 = 0.0f;
                                }
                                float position2 = position + widget4.mListAnchors[offset].getMargin();
                                widget4.mListAnchors[offset].getResolutionNode().resolve(firstNode.resolvedTarget, position2);
                                numMatchConstraints = numMatchConstraints3;
                                widget4.mListAnchors[offset + 1].getResolutionNode().resolve(firstNode.resolvedTarget, position2 + dimension3);
                                widget4.mListAnchors[offset].getResolutionNode().addResolvedValue(system);
                                widget4.mListAnchors[offset + 1].getResolutionNode().addResolvedValue(system);
                                position = position2 + dimension3 + widget4.mListAnchors[offset + 1].getMargin();
                            } else {
                                numMatchConstraints = numMatchConstraints3;
                            }
                            widget4 = next5;
                            lastNode2 = lastNode;
                            extraMargin2 = extraMargin;
                            numMatchConstraints3 = numMatchConstraints;
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public static void setOptimizedWidget(ConstraintWidget widget, int orientation, int resolvedOffset) {
        int startOffset = orientation * 2;
        int endOffset = startOffset + 1;
        widget.mListAnchors[startOffset].getResolutionNode().resolvedTarget = widget.getParent().mLeft.getResolutionNode();
        widget.mListAnchors[startOffset].getResolutionNode().resolvedOffset = resolvedOffset;
        widget.mListAnchors[startOffset].getResolutionNode().state = 1;
        widget.mListAnchors[endOffset].getResolutionNode().resolvedTarget = widget.mListAnchors[startOffset].getResolutionNode();
        widget.mListAnchors[endOffset].getResolutionNode().resolvedOffset = widget.getLength(orientation);
        widget.mListAnchors[endOffset].getResolutionNode().state = 1;
    }
}
