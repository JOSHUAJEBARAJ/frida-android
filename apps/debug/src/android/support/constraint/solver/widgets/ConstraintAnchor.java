/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
    private static final boolean ALLOW_BINARY = false;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    private int mConnectionCreator;
    private ConnectionType mConnectionType;
    int mGoneMargin;
    public int mMargin;
    final ConstraintWidget mOwner;
    private ResolutionAnchor mResolutionAnchor;
    SolverVariable mSolverVariable;
    private Strength mStrength;
    ConstraintAnchor mTarget;
    final Type mType;

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.mResolutionAnchor = new ResolutionAnchor(this);
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.NONE;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mConnectionCreator = 0;
        this.mOwner = constraintWidget;
        this.mType = type;
    }

    private boolean isConnectionToMe(ConstraintWidget object, HashSet<ConstraintWidget> hashSet) {
        if (hashSet.contains(object)) {
            return false;
        }
        hashSet.add((ConstraintWidget)object);
        if (object == this.getOwner()) {
            return true;
        }
        object = object.getAnchors();
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor)object.get(i);
            if (!constraintAnchor.isSimilarDimensionConnection(this) || !constraintAnchor.isConnected() || !this.isConnectionToMe(constraintAnchor.getTarget().getOwner(), hashSet)) continue;
            return true;
        }
        return false;
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, 0, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, int n2) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, n2, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, int n2, Strength strength, int n3, boolean bl) {
        if (constraintAnchor == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!bl && !this.isValidConnection(constraintAnchor)) {
            return false;
        }
        this.mTarget = constraintAnchor;
        this.mMargin = n > 0 ? n : 0;
        this.mGoneMargin = n2;
        this.mStrength = strength;
        this.mConnectionCreator = n3;
        return true;
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, Strength strength, int n2) {
        return this.connect(constraintAnchor, n, -1, strength, n2, false);
    }

    public int getConnectionCreator() {
        return this.mConnectionCreator;
    }

    public ConnectionType getConnectionType() {
        return this.mConnectionType;
    }

    public int getMargin() {
        ConstraintAnchor constraintAnchor;
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && (constraintAnchor = this.mTarget) != null && constraintAnchor.mOwner.getVisibility() == 8) {
            return this.mGoneMargin;
        }
        return this.mMargin;
    }

    public final ConstraintAnchor getOpposite() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 5: {
                return this.mOwner.mTop;
            }
            case 4: {
                return this.mOwner.mBottom;
            }
            case 3: {
                return this.mOwner.mLeft;
            }
            case 2: {
                return this.mOwner.mRight;
            }
            case 1: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
        }
        return null;
    }

    public ConstraintWidget getOwner() {
        return this.mOwner;
    }

    public int getPriorityLevel() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 9: {
                return 0;
            }
            case 8: {
                return 0;
            }
            case 7: {
                return 0;
            }
            case 6: {
                return 1;
            }
            case 5: {
                return 2;
            }
            case 4: {
                return 2;
            }
            case 3: {
                return 2;
            }
            case 2: {
                return 2;
            }
            case 1: 
        }
        return 2;
    }

    public ResolutionAnchor getResolutionNode() {
        return this.mResolutionAnchor;
    }

    public int getSnapPriorityLevel() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 9: {
                return 0;
            }
            case 8: {
                return 1;
            }
            case 7: {
                return 0;
            }
            case 6: {
                return 2;
            }
            case 5: {
                return 0;
            }
            case 4: {
                return 0;
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 1;
            }
            case 1: 
        }
        return 3;
    }

    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }

    public Strength getStrength() {
        return this.mStrength;
    }

    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }

    public Type getType() {
        return this.mType;
    }

    public boolean isConnected() {
        if (this.mTarget != null) {
            return true;
        }
        return false;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget) {
        if (this.isConnectionToMe(constraintWidget, new HashSet<ConstraintWidget>())) {
            return false;
        }
        ConstraintWidget constraintWidget2 = this.getOwner().getParent();
        if (constraintWidget2 == constraintWidget) {
            return true;
        }
        if (constraintWidget.getParent() == constraintWidget2) {
            return true;
        }
        return false;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget, ConstraintAnchor constraintAnchor) {
        return this.isConnectionAllowed(constraintWidget);
    }

    public boolean isSideAnchor() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                return true;
            }
            case 1: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
        }
        return false;
    }

    public boolean isSimilarDimensionConnection(ConstraintAnchor object) {
        object = object.getType();
        Type type = this.mType;
        boolean bl = true;
        boolean bl2 = true;
        if (object == type) {
            return true;
        }
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 9: {
                return false;
            }
            case 4: 
            case 5: 
            case 6: 
            case 8: {
                boolean bl3 = bl2;
                if (object != Type.TOP) {
                    bl3 = bl2;
                    if (object != Type.BOTTOM) {
                        bl3 = bl2;
                        if (object != Type.CENTER_Y) {
                            if (object == Type.BASELINE) {
                                return true;
                            }
                            bl3 = false;
                        }
                    }
                }
                return bl3;
            }
            case 2: 
            case 3: 
            case 7: {
                boolean bl4 = bl;
                if (object != Type.LEFT) {
                    bl4 = bl;
                    if (object != Type.RIGHT) {
                        if (object == Type.CENTER_X) {
                            return true;
                        }
                        bl4 = false;
                    }
                }
                return bl4;
            }
            case 1: 
        }
        if (object != Type.BASELINE) {
            return true;
        }
        return false;
    }

    public boolean isSnapCompatibleWith(ConstraintAnchor constraintAnchor) {
        if (this.mType == Type.CENTER) {
            return false;
        }
        if (this.mType == constraintAnchor.getType()) {
            return true;
        }
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 8: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 4) {
                    if (n != 5) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 7: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 2) {
                    if (n != 3) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 5: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 4) {
                    if (n != 8) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 4: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 5) {
                    if (n != 8) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 3: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 2) {
                    if (n != 7) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 2: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 3) {
                    if (n != 7) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case 1: 
            case 6: 
            case 9: 
        }
        return false;
    }

    public boolean isValidConnection(ConstraintAnchor constraintAnchor) {
        Type type;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (constraintAnchor == null) {
            return false;
        }
        Type type2 = constraintAnchor.getType();
        if (type2 == (type = this.mType)) {
            if (!(type != Type.BASELINE || constraintAnchor.getOwner().hasBaseline() && this.getOwner().hasBaseline())) {
                return false;
            }
            return true;
        }
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                return false;
            }
            case 4: 
            case 5: {
                boolean bl4 = type2 == Type.TOP || type2 == Type.BOTTOM;
                bl2 = bl4;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    block18 : {
                        if (!bl4) {
                            bl4 = bl3;
                            if (type2 != Type.CENTER_Y) break block18;
                        }
                        bl4 = true;
                    }
                    bl2 = bl4;
                }
                return bl2;
            }
            case 2: 
            case 3: {
                boolean bl5 = type2 == Type.LEFT || type2 == Type.RIGHT;
                bl2 = bl5;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    block19 : {
                        if (!bl5) {
                            bl5 = bl;
                            if (type2 != Type.CENTER_X) break block19;
                        }
                        bl5 = true;
                    }
                    bl2 = bl5;
                }
                return bl2;
            }
            case 1: 
        }
        boolean bl6 = bl2;
        if (type2 != Type.BASELINE) {
            bl6 = bl2;
            if (type2 != Type.CENTER_X) {
                bl6 = bl2;
                if (type2 != Type.CENTER_Y) {
                    bl6 = true;
                }
            }
        }
        return bl6;
    }

    public boolean isVerticalAnchor() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
            case 4: 
            case 5: 
            case 6: 
            case 8: 
            case 9: {
                return true;
            }
            case 1: 
            case 2: 
            case 3: 
            case 7: 
        }
        return false;
    }

    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.STRONG;
        this.mConnectionCreator = 0;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mResolutionAnchor.reset();
    }

    public void resetSolverVariable(Cache object) {
        object = this.mSolverVariable;
        if (object == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
            return;
        }
        object.reset();
    }

    public void setConnectionCreator(int n) {
        this.mConnectionCreator = n;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.mConnectionType = connectionType;
    }

    public void setGoneMargin(int n) {
        if (this.isConnected()) {
            this.mGoneMargin = n;
        }
    }

    public void setMargin(int n) {
        if (this.isConnected()) {
            this.mMargin = n;
        }
    }

    public void setStrength(Strength strength) {
        if (this.isConnected()) {
            this.mStrength = strength;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mOwner.getDebugName());
        stringBuilder.append(":");
        stringBuilder.append(this.mType.toString());
        return stringBuilder.toString();
    }

    public static enum ConnectionType {
        RELAXED,
        STRICT;
        

        private ConnectionType() {
        }
    }

    public static enum Strength {
        NONE,
        STRONG,
        WEAK;
        

        private Strength() {
        }
    }

    public static enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y;
        

        private Type() {
        }
    }

}

