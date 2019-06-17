/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;

public class ArrayRow
implements LinearSystem.Row {
    private static final boolean DEBUG = false;
    private static final float epsilon = 0.001f;
    float constantValue = 0.0f;
    boolean isSimpleDefinition = false;
    boolean used = false;
    SolverVariable variable = null;
    public final ArrayLinkedVariables variables;

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }

    public ArrayRow addError(LinearSystem linearSystem, int n) {
        this.variables.put(linearSystem.createErrorVariable(n, "ep"), 1.0f);
        this.variables.put(linearSystem.createErrorVariable(n, "em"), -1.0f);
        return this;
    }

    @Override
    public void addError(SolverVariable solverVariable) {
        float f = 1.0f;
        if (solverVariable.strength == 1) {
            f = 1.0f;
        } else if (solverVariable.strength == 2) {
            f = 1000.0f;
        } else if (solverVariable.strength == 3) {
            f = 1000000.0f;
        } else if (solverVariable.strength == 4) {
            f = 1.0E9f;
        } else if (solverVariable.strength == 5) {
            f = 1.0E12f;
        }
        this.variables.put(solverVariable, f);
    }

    ArrayRow addSingleError(SolverVariable solverVariable, int n) {
        this.variables.put(solverVariable, n);
        return this;
    }

    boolean chooseSubject(LinearSystem object) {
        boolean bl = false;
        if ((object = this.variables.chooseSubject((LinearSystem)object)) == null) {
            bl = true;
        } else {
            this.pivot((SolverVariable)object);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
        return bl;
    }

    @Override
    public void clear() {
        this.variables.clear();
        this.variable = null;
        this.constantValue = 0.0f;
    }

    ArrayRow createRowCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
            return this;
        }
        if (f == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n > 0 || n2 > 0) {
                this.constantValue = - n + n2;
                return this;
            }
        } else {
            if (f <= 0.0f) {
                this.variables.put(solverVariable, -1.0f);
                this.variables.put(solverVariable2, 1.0f);
                this.constantValue = n;
                return this;
            }
            if (f >= 1.0f) {
                this.variables.put(solverVariable3, -1.0f);
                this.variables.put(solverVariable4, 1.0f);
                this.constantValue = n2;
                return this;
            }
            this.variables.put(solverVariable, (1.0f - f) * 1.0f);
            this.variables.put(solverVariable2, (1.0f - f) * -1.0f);
            this.variables.put(solverVariable3, -1.0f * f);
            this.variables.put(solverVariable4, f * 1.0f);
            if (n > 0 || n2 > 0) {
                this.constantValue = (float)(- n) * (1.0f - f) + (float)n2 * f;
            }
        }
        return this;
    }

    ArrayRow createRowDefinition(SolverVariable solverVariable, int n) {
        this.variable = solverVariable;
        solverVariable.computedValue = n;
        this.constantValue = n;
        this.isSimpleDefinition = true;
        return this;
    }

    ArrayRow createRowDimensionPercent(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, float f) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - f);
        this.variables.put(solverVariable3, f);
        return this;
    }

    public ArrayRow createRowDimensionRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, f);
        this.variables.put(solverVariable4, - f);
        return this;
    }

    public ArrayRow createRowEqualDimension(float f, float f2, float f3, SolverVariable solverVariable, int n, SolverVariable solverVariable2, int n2, SolverVariable solverVariable3, int n3, SolverVariable solverVariable4, int n4) {
        if (f2 != 0.0f && f != f3) {
            f = f / f2 / (f3 / f2);
            this.constantValue = (float)(- n - n2) + (float)n3 * f + (float)n4 * f;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, f);
            this.variables.put(solverVariable3, - f);
            return this;
        }
        this.constantValue = - n - n2 + n3 + n4;
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable4, 1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }

    public ArrayRow createRowEqualMatchDimensions(float f, float f2, float f3, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4) {
        this.constantValue = 0.0f;
        if (f2 != 0.0f && f != f3) {
            if (f == 0.0f) {
                this.variables.put(solverVariable, 1.0f);
                this.variables.put(solverVariable2, -1.0f);
                return this;
            }
            if (f3 == 0.0f) {
                this.variables.put(solverVariable3, 1.0f);
                this.variables.put(solverVariable4, -1.0f);
                return this;
            }
            f = f / f2 / (f3 / f2);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, f);
            this.variables.put(solverVariable3, - f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable4, 1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable solverVariable, int n) {
        if (n < 0) {
            this.constantValue = n * -1;
            this.variables.put(solverVariable, 1.0f);
            return this;
        }
        this.constantValue = n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable solverVariable, SolverVariable solverVariable2, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            n2 = n;
            n = n3;
            n3 = n2;
            if (n2 < 0) {
                n3 = n2 * -1;
                n = 1;
            }
            this.constantValue = n3;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable solverVariable, int n, SolverVariable solverVariable2) {
        this.constantValue = n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            n2 = n;
            n = n3;
            n3 = n2;
            if (n2 < 0) {
                n3 = n2 * -1;
                n = 1;
            }
            this.constantValue = n3;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }

    public ArrayRow createRowLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            n2 = n;
            n = n3;
            n3 = n2;
            if (n2 < 0) {
                n3 = n2 * -1;
                n = 1;
            }
            this.constantValue = n3;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, 1.0f);
        return this;
    }

    public ArrayRow createRowWithAngle(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f) {
        this.variables.put(solverVariable3, 0.5f);
        this.variables.put(solverVariable4, 0.5f);
        this.variables.put(solverVariable, -0.5f);
        this.variables.put(solverVariable2, -0.5f);
        this.constantValue = - f;
        return this;
    }

    void ensurePositiveConstant() {
        float f = this.constantValue;
        if (f < 0.0f) {
            this.constantValue = f * -1.0f;
            this.variables.invert();
        }
    }

    @Override
    public SolverVariable getKey() {
        return this.variable;
    }

    @Override
    public SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] arrbl) {
        return this.variables.getPivotCandidate(arrbl, null);
    }

    boolean hasKeyVariable() {
        SolverVariable solverVariable = this.variable;
        if (solverVariable != null && (solverVariable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f)) {
            return true;
        }
        return false;
    }

    boolean hasVariable(SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }

    @Override
    public void initFromRow(LinearSystem.Row row) {
        if (row instanceof ArrayRow) {
            row = (ArrayRow)row;
            this.variable = null;
            this.variables.clear();
            for (int i = 0; i < row.variables.currentSize; ++i) {
                SolverVariable solverVariable = row.variables.getVariable(i);
                float f = row.variables.getVariableValue(i);
                this.variables.add(solverVariable, f, true);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        if (this.variable == null && this.constantValue == 0.0f && this.variables.currentSize == 0) {
            return true;
        }
        return false;
    }

    SolverVariable pickPivot(SolverVariable solverVariable) {
        return this.variables.getPivotCandidate(null, solverVariable);
    }

    void pivot(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.variable;
        if (solverVariable2 != null) {
            this.variables.put(solverVariable2, -1.0f);
            this.variable = null;
        }
        float f = this.variables.remove(solverVariable, true) * -1.0f;
        this.variable = solverVariable;
        if (f == 1.0f) {
            return;
        }
        this.constantValue /= f;
        this.variables.divideByAmount(f);
    }

    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }

    int sizeInBytes() {
        int n = 0;
        if (this.variable != null) {
            n = 0 + 4;
        }
        return n + 4 + 4 + this.variables.sizeInBytes();
    }

    String toReadableString() {
        CharSequence charSequence2;
        CharSequence charSequence2;
        if (this.variable == null) {
            charSequence2 = new StringBuilder();
            charSequence2.append("");
            charSequence2.append("0");
            charSequence2 = charSequence2.toString();
        } else {
            charSequence2 = new StringBuilder();
            charSequence2.append("");
            charSequence2.append(this.variable);
            charSequence2 = charSequence2.toString();
        }
        Object object = new StringBuilder();
        object.append((String)charSequence2);
        object.append(" = ");
        object = object.toString();
        boolean bl = false;
        charSequence2 = object;
        if (this.constantValue != 0.0f) {
            charSequence2 = new StringBuilder();
            charSequence2.append((String)object);
            charSequence2.append(this.constantValue);
            charSequence2 = charSequence2.toString();
            bl = true;
        }
        int n = this.variables.currentSize;
        for (int i = 0; i < n; ++i) {
            float f;
            float f2;
            object = this.variables.getVariable(i);
            if (object == null || (f = this.variables.getVariableValue(i)) == 0.0f) continue;
            String string2 = object.toString();
            if (!bl) {
                object = charSequence2;
                f2 = f;
                if (f < 0.0f) {
                    object = new StringBuilder();
                    object.append((String)charSequence2);
                    object.append("- ");
                    object = object.toString();
                    f2 = f * -1.0f;
                }
            } else if (f > 0.0f) {
                object = new StringBuilder();
                object.append((String)charSequence2);
                object.append(" + ");
                object = object.toString();
                f2 = f;
            } else {
                object = new StringBuilder();
                object.append((String)charSequence2);
                object.append(" - ");
                object = object.toString();
                f2 = f * -1.0f;
            }
            if (f2 == 1.0f) {
                charSequence2 = new StringBuilder();
                charSequence2.append((String)object);
                charSequence2.append(string2);
                charSequence2 = charSequence2.toString();
            } else {
                charSequence2 = new StringBuilder();
                charSequence2.append((String)object);
                charSequence2.append(f2);
                charSequence2.append(" ");
                charSequence2.append(string2);
                charSequence2 = charSequence2.toString();
            }
            bl = true;
        }
        object = charSequence2;
        if (!bl) {
            object = new StringBuilder();
            object.append((String)charSequence2);
            object.append("0.0");
            object = object.toString();
        }
        return object;
    }

    public String toString() {
        return this.toReadableString();
    }
}

