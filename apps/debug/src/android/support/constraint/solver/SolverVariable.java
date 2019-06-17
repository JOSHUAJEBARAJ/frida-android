/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.ArrayRow;
import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 7;
    public static final int STRENGTH_BARRIER = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueConstantId;
    private static int uniqueErrorId;
    private static int uniqueId;
    private static int uniqueSlackId;
    private static int uniqueUnrestrictedId;
    public float computedValue;
    int definitionId = -1;
    public int id = -1;
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[7];
    public int usageInRowCount = 0;

    static {
        uniqueSlackId = 1;
        uniqueErrorId = 1;
        uniqueUnrestrictedId = 1;
        uniqueConstantId = 1;
        uniqueId = 1;
    }

    public SolverVariable(Type type, String string2) {
        this.mType = type;
    }

    public SolverVariable(String string2, Type type) {
        this.mName = string2;
        this.mType = type;
    }

    private static String getUniqueName(Type object, String string2) {
        if (string2 != null) {
            object = new StringBuilder();
            object.append(string2);
            object.append(uniqueErrorId);
            return object.toString();
        }
        int n = .$SwitchMap$android$support$constraint$solver$SolverVariable$Type[object.ordinal()];
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n == 5) {
                            object = new StringBuilder();
                            object.append("V");
                            uniqueId = n = uniqueId + 1;
                            object.append(n);
                            return object.toString();
                        }
                        throw new AssertionError((Object)object.name());
                    }
                    object = new StringBuilder();
                    object.append("e");
                    uniqueErrorId = n = uniqueErrorId + 1;
                    object.append(n);
                    return object.toString();
                }
                object = new StringBuilder();
                object.append("S");
                uniqueSlackId = n = uniqueSlackId + 1;
                object.append(n);
                return object.toString();
            }
            object = new StringBuilder();
            object.append("C");
            uniqueConstantId = n = uniqueConstantId + 1;
            object.append(n);
            return object.toString();
        }
        object = new StringBuilder();
        object.append("U");
        uniqueUnrestrictedId = n = uniqueUnrestrictedId + 1;
        object.append(n);
        return object.toString();
    }

    static void increaseErrorId() {
        ++uniqueErrorId;
    }

    public final void addToRow(ArrayRow arrayRow) {
        int n;
        int n2;
        for (n2 = 0; n2 < (n = this.mClientEquationsCount); ++n2) {
            if (this.mClientEquations[n2] != arrayRow) continue;
            return;
        }
        ArrayRow[] arrarrayRow = this.mClientEquations;
        if (n >= arrarrayRow.length) {
            this.mClientEquations = Arrays.copyOf(arrarrayRow, arrarrayRow.length * 2);
        }
        arrarrayRow = this.mClientEquations;
        n2 = this.mClientEquationsCount;
        arrarrayRow[n2] = arrayRow;
        this.mClientEquationsCount = n2 + 1;
    }

    void clearStrengths() {
        for (int i = 0; i < 7; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }

    public String getName() {
        return this.mName;
    }

    public final void removeFromRow(ArrayRow arrarrayRow) {
        int n = this.mClientEquationsCount;
        for (int i = 0; i < n; ++i) {
            if (this.mClientEquations[i] != arrarrayRow) continue;
            for (int j = 0; j < n - i - 1; ++j) {
                arrarrayRow = this.mClientEquations;
                arrarrayRow[i + j] = arrarrayRow[i + j + 1];
            }
            --this.mClientEquationsCount;
            return;
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }

    public void setName(String string2) {
        this.mName = string2;
    }

    public void setType(Type type, String string2) {
        this.mType = type;
    }

    String strengthsToString() {
        Object object;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this);
        charSequence.append("[");
        charSequence = charSequence.toString();
        boolean bl = false;
        boolean bl2 = true;
        for (int i = 0; i < this.strengthVector.length; ++i) {
            object = new StringBuilder();
            object.append((String)charSequence);
            object.append(this.strengthVector[i]);
            charSequence = object.toString();
            object = this.strengthVector;
            if (object[i] > 0.0f) {
                bl = false;
            } else if (object[i] < 0.0f) {
                bl = true;
            }
            if (this.strengthVector[i] != 0.0f) {
                bl2 = false;
            }
            if (i < this.strengthVector.length - 1) {
                object = new StringBuilder();
                object.append((String)charSequence);
                object.append(", ");
                charSequence = object.toString();
                continue;
            }
            object = new StringBuilder();
            object.append((String)charSequence);
            object.append("] ");
            charSequence = object.toString();
        }
        object = charSequence;
        if (bl) {
            object = new StringBuilder();
            object.append((String)charSequence);
            object.append(" (-)");
            object = object.toString();
        }
        charSequence = object;
        if (bl2) {
            charSequence = new StringBuilder();
            charSequence.append((String)object);
            charSequence.append(" (*)");
            charSequence = charSequence.toString();
        }
        return charSequence;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.mName);
        return stringBuilder.toString();
    }

    public final void updateReferencesWithNewDefinition(ArrayRow arrayRow) {
        int n = this.mClientEquationsCount;
        for (int i = 0; i < n; ++i) {
            this.mClientEquations[i].variables.updateFromRow(this.mClientEquations[i], arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }

    public static enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN;
        

        private Type() {
        }
    }

}

