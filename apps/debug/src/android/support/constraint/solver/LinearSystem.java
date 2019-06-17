/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.GoalRow;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.Pools;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
    private static final boolean DEBUG = false;
    public static final boolean FULL_DEBUG = false;
    private static int POOL_SIZE = 1000;
    public static Metrics sMetrics;
    private int TABLE_SIZE;
    public boolean graphOptimizer;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    ArrayRow[] mRows;
    private final Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables = null;
    int mVariablesID = 0;
    private ArrayRow[] tempClientsCopy;

    public LinearSystem() {
        int n;
        this.mMaxColumns = n = (this.TABLE_SIZE = 32);
        this.mRows = null;
        this.graphOptimizer = false;
        this.mAlreadyTestedCandidates = new boolean[n];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = n;
        this.mPoolVariables = new SolverVariable[POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[n];
        this.mRows = new ArrayRow[n];
        this.releaseRows();
        this.mCache = new Cache();
        this.mGoal = new GoalRow(this.mCache);
        this.mTempGoal = new ArrayRow(this.mCache);
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type object, String arrsolverVariable) {
        SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        if (solverVariable == null) {
            solverVariable = new SolverVariable((SolverVariable.Type)((Object)object), (String)arrsolverVariable);
            solverVariable.setType((SolverVariable.Type)((Object)object), (String)arrsolverVariable);
            object = solverVariable;
        } else {
            solverVariable.reset();
            solverVariable.setType((SolverVariable.Type)((Object)object), (String)arrsolverVariable);
            object = solverVariable;
        }
        int n = this.mPoolVariablesCount;
        int n2 = POOL_SIZE;
        if (n >= n2) {
            POOL_SIZE = n2 * 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
        }
        arrsolverVariable = this.mPoolVariables;
        n = this.mPoolVariablesCount;
        this.mPoolVariablesCount = n + 1;
        arrsolverVariable[n] = object;
        return object;
    }

    private void addError(ArrayRow arrayRow) {
        arrayRow.addError(this, 0);
    }

    private final void addRow(ArrayRow arrayRow) {
        int n;
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = arrayRow;
        SolverVariable solverVariable = arrayRow.variable;
        solverVariable.definitionId = n = this.mNumRows;
        this.mNumRows = n + 1;
        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
    }

    private void addSingleError(ArrayRow arrayRow, int n) {
        this.addSingleError(arrayRow, n, 0);
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public static ArrayRow createRowCentering(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2, boolean bl) {
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowCentering(solverVariable, solverVariable2, n, f, solverVariable3, solverVariable4, n2);
        if (bl) {
            arrayRow.addError(linearSystem, 4);
            return arrayRow;
        }
        return arrayRow;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, float f, boolean bl) {
        ArrayRow arrayRow = linearSystem.createRow();
        if (bl) {
            linearSystem.addError(arrayRow);
        }
        return arrayRow.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, f);
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowEquals(solverVariable, solverVariable2, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, 1);
        }
        return arrayRow;
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        SolverVariable solverVariable3 = linearSystem.createSlackVariable();
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)));
        }
        return arrayRow;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        SolverVariable solverVariable3 = linearSystem.createSlackVariable();
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)));
        }
        return arrayRow;
    }

    private SolverVariable createVariable(String string2, SolverVariable.Type object) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.variables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable((SolverVariable.Type)((Object)object), null);
        object.setName(string2);
        ++this.mVariablesID;
        ++this.mNumColumns;
        object.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        this.mVariables.put(string2, (SolverVariable)object);
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    private void displayRows() {
        StringBuilder stringBuilder;
        this.displaySolverVariables();
        String string2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mRows[i]);
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(this.mGoal);
        stringBuilder.append("\n");
        string2 = stringBuilder.toString();
        System.out.println(string2);
    }

    private void displaySolverVariables() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append("Display Rows (");
        charSequence.append(this.mNumRows);
        charSequence.append("x");
        charSequence.append(this.mNumColumns);
        charSequence.append(")\n");
        charSequence = charSequence.toString();
        System.out.println((String)charSequence);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int enforceBFS(Row var1_1) throws Exception {
        var7_2 = 0;
        var8_3 = 0;
        var6_4 = 0;
        do {
            var5_5 = var8_3;
            if (var6_4 >= this.mNumRows) break;
            if (this.mRows[var6_4].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[var6_4].constantValue < 0.0f) {
                var5_5 = 1;
                break;
            }
            ++var6_4;
        } while (true);
        var6_4 = var7_2;
        if (var5_5 == 0) return var6_4;
        var8_3 = 0;
        var5_5 = 0;
        block1 : do {
            var6_4 = var5_5;
            if (var8_3 != 0) return var6_4;
            var1_1 = LinearSystem.sMetrics;
            if (var1_1 != null) {
                ++var1_1.bfs;
            }
            var15_15 = var5_5 + 1;
            var2_6 = Float.MAX_VALUE;
            var5_5 = 0;
            var6_4 = -1;
            var7_2 = -1;
            var9_9 = 0;
            do {
                if (var9_9 >= this.mNumRows) ** GOTO lbl49
                var1_1 = this.mRows[var9_9];
                if (var1_1.variable.mType != SolverVariable.Type.UNRESTRICTED) ** GOTO lbl37
                var3_7 = var2_6;
                var11_11 = var5_5;
                var12_12 = var6_4;
                var13_13 = var7_2;
                ** GOTO lbl106
lbl37: // 1 sources:
                if (!var1_1.isSimpleDefinition) ** GOTO lbl43
                var3_7 = var2_6;
                var11_11 = var5_5;
                var12_12 = var6_4;
                var13_13 = var7_2;
                ** GOTO lbl106
lbl43: // 1 sources:
                var3_7 = var2_6;
                var11_11 = var5_5;
                var12_12 = var6_4;
                var13_13 = var7_2;
                if (var1_1.constantValue >= 0.0f) ** GOTO lbl106
                ** GOTO lbl64
lbl49: // 1 sources:
                if (var6_4 != -1) {
                    var1_1 = this.mRows[var6_4];
                    var1_1.variable.definitionId = -1;
                    var16_16 = LinearSystem.sMetrics;
                    if (var16_16 != null) {
                        ++var16_16.pivots;
                    }
                    var1_1.pivot(this.mCache.mIndexedVariables[var7_2]);
                    var1_1.variable.definitionId = var6_4;
                    var1_1.variable.updateReferencesWithNewDefinition((ArrayRow)var1_1);
                } else {
                    var8_3 = 1;
                }
                if (var15_15 > this.mNumColumns / 2) {
                    var8_3 = 1;
                }
                var5_5 = var15_15;
                continue block1;
lbl64: // 1 sources:
                var10_10 = 1;
                do {
                    var3_7 = var2_6;
                    var11_11 = var5_5;
                    var12_12 = var6_4;
                    var13_13 = var7_2;
                    if (var10_10 >= this.mNumColumns) break;
                    var16_16 = this.mCache.mIndexedVariables[var10_10];
                    var4_8 = var1_1.variables.get((SolverVariable)var16_16);
                    if (var4_8 > 0.0f) ** GOTO lbl79
                    var3_7 = var2_6;
                    var12_12 = var5_5;
                    var13_13 = var6_4;
                    var14_14 = var7_2;
                    ** GOTO lbl100
lbl79: // 1 sources:
                    var12_12 = 0;
                    var11_11 = var6_4;
                    var6_4 = var5_5;
                    var5_5 = var12_12;
                    do {
                        var3_7 = var2_6;
                        var12_12 = var6_4;
                        var13_13 = var11_11;
                        var14_14 = var7_2;
                        if (var5_5 >= 7) break;
                        var3_7 = var16_16.strengthVector[var5_5] / var4_8;
                        if (var3_7 < var2_6 && var5_5 == var6_4) ** GOTO lbl-1000
                        var12_12 = var6_4;
                        if (var5_5 > var6_4) lbl-1000: // 2 sources:
                        {
                            var2_6 = var3_7;
                            var11_11 = var9_9;
                            var7_2 = var10_10;
                            var12_12 = var5_5;
                        }
                        ++var5_5;
                        var6_4 = var12_12;
                    } while (true);
lbl100: // 2 sources:
                    ++var10_10;
                    var2_6 = var3_7;
                    var5_5 = var12_12;
                    var6_4 = var13_13;
                    var7_2 = var14_14;
                } while (true);
lbl106: // 4 sources:
                ++var9_9;
                var2_6 = var3_7;
                var5_5 = var11_11;
                var6_4 = var12_12;
                var7_2 = var13_13;
            } while (true);
            break;
        } while (true);
    }

    private String getDisplaySize(int n) {
        int n2 = n * 4 / 1024 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Mb");
            return stringBuilder.toString();
        }
        n2 = n * 4 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Kb");
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(n * 4);
        stringBuilder.append(" bytes");
        return stringBuilder.toString();
    }

    private String getDisplayStrength(int n) {
        if (n == 1) {
            return "LOW";
        }
        if (n == 2) {
            return "MEDIUM";
        }
        if (n == 3) {
            return "HIGH";
        }
        if (n == 4) {
            return "HIGHEST";
        }
        if (n == 5) {
            return "EQUALITY";
        }
        if (n == 6) {
            return "FIXED";
        }
        return "NONE";
    }

    public static Metrics getMetrics() {
        return sMetrics;
    }

    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        Object object = this.mCache;
        object.mIndexedVariables = Arrays.copyOf(object.mIndexedVariables, this.TABLE_SIZE);
        int n = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[n];
        this.mMaxColumns = n;
        this.mMaxRows = n;
        object = sMetrics;
        if (object != null) {
            ++object.tableSizeIncrease;
            object = sMetrics;
            object.maxTableSize = Math.max(object.maxTableSize, (long)this.TABLE_SIZE);
            object = sMetrics;
            object.lastTableSize = object.maxTableSize;
        }
    }

    private final int optimize(Row row, boolean bl) {
        int n;
        int n2;
        Object object = sMetrics;
        if (object != null) {
            ++object.optimize;
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        do {
            n2 = n3;
            n = n4;
            if (n5 >= this.mNumColumns) break;
            this.mAlreadyTestedCandidates[n5] = false;
            ++n5;
        } while (true);
        while (n2 == 0) {
            object = sMetrics;
            if (object != null) {
                ++object.iterations;
            }
            if ((n4 = n + 1) >= this.mNumColumns * 2) {
                return n4;
            }
            if (row.getKey() != null) {
                this.mAlreadyTestedCandidates[row.getKey().id] = true;
            }
            if ((object = row.getPivotCandidate(this, this.mAlreadyTestedCandidates)) != null) {
                if (this.mAlreadyTestedCandidates[object.id]) {
                    return n4;
                }
                this.mAlreadyTestedCandidates[object.id] = true;
            }
            if (object != null) {
                ArrayRow arrayRow;
                float f = Float.MAX_VALUE;
                n = -1;
                for (n5 = 0; n5 < this.mNumRows; ++n5) {
                    float f2;
                    arrayRow = this.mRows[n5];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        f2 = f;
                        n3 = n;
                    } else if (arrayRow.isSimpleDefinition) {
                        f2 = f;
                        n3 = n;
                    } else {
                        f2 = f;
                        n3 = n;
                        if (arrayRow.hasVariable((SolverVariable)object)) {
                            float f3 = arrayRow.variables.get((SolverVariable)object);
                            f2 = f;
                            n3 = n;
                            if (f3 < 0.0f) {
                                f3 = (- arrayRow.constantValue) / f3;
                                f2 = f;
                                n3 = n;
                                if (f3 < f) {
                                    f2 = f3;
                                    n3 = n5;
                                }
                            }
                        }
                    }
                    f = f2;
                    n = n3;
                }
                if (n > -1) {
                    arrayRow = this.mRows[n];
                    arrayRow.variable.definitionId = -1;
                    Metrics metrics = sMetrics;
                    if (metrics != null) {
                        ++metrics.pivots;
                    }
                    arrayRow.pivot((SolverVariable)object);
                    arrayRow.variable.definitionId = n;
                    arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
                } else {
                    n2 = 1;
                }
            } else {
                n2 = 1;
            }
            n = n4;
        }
        return n;
    }

    private void releaseRows() {
        Object object;
        for (int i = 0; i < (object = this.mRows).length; ++i) {
            if ((object = object[i]) != null) {
                this.mCache.arrayRowPool.release((ArrayRow)object);
            }
            this.mRows[i] = null;
        }
    }

    private final void updateRowFromVariables(ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }

    public void addCenterPoint(ConstraintWidget object, ConstraintWidget object2, float f, int n) {
        SolverVariable solverVariable = this.createObjectVariable(object.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariable2 = this.createObjectVariable(object.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable solverVariable3 = this.createObjectVariable(object.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable solverVariable4 = this.createObjectVariable(object.getAnchor(ConstraintAnchor.Type.BOTTOM));
        object = this.createObjectVariable(object2.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariable5 = this.createObjectVariable(object2.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable solverVariable6 = this.createObjectVariable(object2.getAnchor(ConstraintAnchor.Type.RIGHT));
        object2 = this.createObjectVariable(object2.getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow arrayRow = this.createRow();
        double d = Math.sin(f);
        double d2 = n;
        Double.isNaN(d2);
        arrayRow.createRowWithAngle(solverVariable2, solverVariable4, solverVariable5, (SolverVariable)object2, (float)(d * d2));
        this.addConstraint(arrayRow);
        object2 = this.createRow();
        d = Math.cos(f);
        d2 = n;
        Double.isNaN(d2);
        object2.createRowWithAngle(solverVariable, solverVariable3, (SolverVariable)object, solverVariable6, (float)(d * d2));
        this.addConstraint((ArrayRow)object2);
    }

    public void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2, int n3) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowCentering(solverVariable, solverVariable2, n, f, solverVariable3, solverVariable4, n2);
        if (n3 != 6) {
            arrayRow.addError(this, n3);
        }
        this.addConstraint(arrayRow);
    }

    public void addConstraint(ArrayRow arrayRow) {
        if (arrayRow == null) {
            return;
        }
        Object object = sMetrics;
        if (object != null) {
            ++object.constraints;
            if (arrayRow.isSimpleDefinition) {
                object = sMetrics;
                ++object.simpleconstraints;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        boolean bl = false;
        boolean bl2 = false;
        if (!arrayRow.isSimpleDefinition) {
            this.updateRowFromVariables(arrayRow);
            if (arrayRow.isEmpty()) {
                return;
            }
            arrayRow.ensurePositiveConstant();
            bl = bl2;
            if (arrayRow.chooseSubject(this)) {
                arrayRow.variable = object = this.createExtraVariable();
                this.addRow(arrayRow);
                bl2 = true;
                this.mTempGoal.initFromRow(arrayRow);
                this.optimize(this.mTempGoal, true);
                bl = bl2;
                if (object.definitionId == -1) {
                    if (arrayRow.variable == object && (object = arrayRow.pickPivot((SolverVariable)object)) != null) {
                        Metrics metrics = sMetrics;
                        if (metrics != null) {
                            ++metrics.pivots;
                        }
                        arrayRow.pivot((SolverVariable)object);
                    }
                    if (!arrayRow.isSimpleDefinition) {
                        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
                    }
                    --this.mNumRows;
                    bl = bl2;
                }
            }
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (!bl) {
            this.addRow(arrayRow);
        }
    }

    public ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowEquals(solverVariable, solverVariable2, n);
        if (n2 != 6) {
            arrayRow.addError(this, n2);
        }
        this.addConstraint(arrayRow);
        return arrayRow;
    }

    public void addEquality(SolverVariable solverVariable, int n) {
        int n2 = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            ArrayRow arrayRow = this.mRows[n2];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = n;
            } else if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
                arrayRow.constantValue = n;
            } else {
                arrayRow = this.createRow();
                arrayRow.createRowEquals(solverVariable, n);
                this.addConstraint(arrayRow);
            }
            return;
        }
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowDefinition(solverVariable, n);
        this.addConstraint(arrayRow);
    }

    public void addEquality(SolverVariable solverVariable, int n, int n2) {
        int n3 = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            ArrayRow arrayRow = this.mRows[n3];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = n;
            } else {
                arrayRow = this.createRow();
                arrayRow.createRowEquals(solverVariable, n);
                arrayRow.addError(this, n2);
                this.addConstraint(arrayRow);
            }
            return;
        }
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowDefinition(solverVariable, n);
        arrayRow.addError(this, n2);
        this.addConstraint(arrayRow);
    }

    public void addGreaterBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, boolean bl) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, 0);
        if (bl) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), 1);
        }
        this.addConstraint(arrayRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, int n) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable2 = this.createSlackVariable();
        solverVariable2.strength = 0;
        arrayRow.createRowGreaterThan(solverVariable, n, solverVariable2);
        this.addConstraint(arrayRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        if (n2 != 6) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), n2);
        }
        this.addConstraint(arrayRow);
    }

    public void addLowerBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, boolean bl) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, 0);
        if (bl) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), 1);
        }
        this.addConstraint(arrayRow);
    }

    public void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        if (n2 != 6) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), n2);
        }
        this.addConstraint(arrayRow);
    }

    public void addRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f, int n) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, f);
        if (n != 6) {
            arrayRow.addError(this, n);
        }
        this.addConstraint(arrayRow);
    }

    void addSingleError(ArrayRow arrayRow, int n, int n2) {
        arrayRow.addSingleError(this.createErrorVariable(n2, null), n);
    }

    public SolverVariable createErrorVariable(int n, String object) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.errors;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.ERROR, (String)object);
        ++this.mVariablesID;
        ++this.mNumColumns;
        object.id = this.mVariablesID;
        object.strength = n;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        this.mGoal.addError((SolverVariable)object);
        return object;
    }

    public SolverVariable createExtraVariable() {
        Object object = sMetrics;
        if (object != null) {
            ++object.extravariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        ++this.mVariablesID;
        ++this.mNumColumns;
        object.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public SolverVariable createObjectVariable(Object object) {
        if (object == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        SolverVariable solverVariable = null;
        if (!(object instanceof ConstraintAnchor)) return solverVariable;
        SolverVariable solverVariable2 = solverVariable = ((ConstraintAnchor)object).getSolverVariable();
        if (solverVariable == null) {
            ((ConstraintAnchor)object).resetSolverVariable(this.mCache);
            solverVariable2 = ((ConstraintAnchor)object).getSolverVariable();
        }
        if (solverVariable2.id != -1 && solverVariable2.id <= this.mVariablesID) {
            solverVariable = solverVariable2;
            if (this.mCache.mIndexedVariables[solverVariable2.id] != null) return solverVariable;
        }
        if (solverVariable2.id != -1) {
            solverVariable2.reset();
        }
        ++this.mVariablesID;
        ++this.mNumColumns;
        solverVariable2.id = this.mVariablesID;
        solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
        return solverVariable2;
    }

    public ArrayRow createRow() {
        ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            arrayRow = new ArrayRow(this.mCache);
        } else {
            arrayRow.reset();
        }
        SolverVariable.increaseErrorId();
        return arrayRow;
    }

    public SolverVariable createSlackVariable() {
        Object object = sMetrics;
        if (object != null) {
            ++object.slackvariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        ++this.mVariablesID;
        ++this.mNumColumns;
        object.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    void displayReadableRows() {
        void var3_8;
        this.displaySolverVariables();
        String string3 = " #  ";
        for (int i = 0; i < this.mNumRows; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(this.mRows[i].toReadableString());
            string3 = stringBuilder.toString();
            StringBuilder string2 = new StringBuilder();
            string2.append(string3);
            string2.append("\n #  ");
            string3 = string2.toString();
        }
        String string2 = string3;
        if (this.mGoal != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(this.mGoal);
            stringBuilder.append("\n");
            String string4 = stringBuilder.toString();
        }
        System.out.println((String)var3_8);
    }

    void displaySystemInformations() {
        int n;
        int n2;
        ArrayRow[] arrarrayRow;
        int n3 = 0;
        for (n2 = 0; n2 < this.TABLE_SIZE; ++n2) {
            arrarrayRow = this.mRows;
            n = n3;
            if (arrarrayRow[n2] != null) {
                n = n3 + arrarrayRow[n2].sizeInBytes();
            }
            n3 = n;
        }
        n2 = 0;
        for (n = 0; n < this.mNumRows; ++n) {
            arrarrayRow = this.mRows;
            int n4 = n2;
            if (arrarrayRow[n] != null) {
                n4 = n2 + arrarrayRow[n].sizeInBytes();
            }
            n2 = n4;
        }
        arrarrayRow = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Linear System -> Table size: ");
        stringBuilder.append(this.TABLE_SIZE);
        stringBuilder.append(" (");
        n = this.TABLE_SIZE;
        stringBuilder.append(this.getDisplaySize(n * n));
        stringBuilder.append(") -- row sizes: ");
        stringBuilder.append(this.getDisplaySize(n3));
        stringBuilder.append(", actual size: ");
        stringBuilder.append(this.getDisplaySize(n2));
        stringBuilder.append(" rows: ");
        stringBuilder.append(this.mNumRows);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxRows);
        stringBuilder.append(" cols: ");
        stringBuilder.append(this.mNumColumns);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxColumns);
        stringBuilder.append(" ");
        stringBuilder.append(0);
        stringBuilder.append(" occupied cells, ");
        stringBuilder.append(this.getDisplaySize(0));
        arrarrayRow.println(stringBuilder.toString());
    }

    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String string3 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            void string2;
            String string4 = string3;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string3);
                stringBuilder.append(this.mRows[i].toReadableString());
                string3 = stringBuilder.toString();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(string3);
                stringBuilder2.append("\n");
                String string5 = stringBuilder2.toString();
            }
            string3 = string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string3);
        stringBuilder.append(this.mGoal);
        stringBuilder.append("\n");
        string3 = stringBuilder.toString();
        System.out.println(string3);
    }

    public void fillMetrics(Metrics metrics) {
        sMetrics = metrics;
    }

    public Cache getCache() {
        return this.mCache;
    }

    Row getGoal() {
        return this.mGoal;
    }

    public int getMemoryUsed() {
        int n = 0;
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow[] arrarrayRow = this.mRows;
            int n2 = n;
            if (arrarrayRow[i] != null) {
                n2 = n + arrarrayRow[i].sizeInBytes();
            }
            n = n2;
        }
        return n;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    public int getObjectVariableValue(Object object) {
        if ((object = ((ConstraintAnchor)object).getSolverVariable()) != null) {
            return (int)(object.computedValue + 0.5f);
        }
        return 0;
    }

    ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    float getValueFor(String object) {
        if ((object = this.getVariable((String)object, SolverVariable.Type.UNRESTRICTED)) == null) {
            return 0.0f;
        }
        return object.computedValue;
    }

    SolverVariable getVariable(String string2, SolverVariable.Type type) {
        SolverVariable solverVariable;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        SolverVariable solverVariable2 = solverVariable = this.mVariables.get(string2);
        if (solverVariable == null) {
            solverVariable2 = this.createVariable(string2, type);
        }
        return solverVariable2;
    }

    public void minimize() throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.minimize;
        }
        if (this.graphOptimizer) {
            boolean bl;
            metrics = sMetrics;
            if (metrics != null) {
                ++metrics.graphOptimizer;
            }
            boolean bl2 = true;
            int n = 0;
            do {
                bl = bl2;
                if (n >= this.mNumRows) break;
                if (!this.mRows[n].isSimpleDefinition) {
                    bl = false;
                    break;
                }
                ++n;
            } while (true);
            if (!bl) {
                this.minimizeGoal(this.mGoal);
            } else {
                metrics = sMetrics;
                if (metrics != null) {
                    ++metrics.fullySolved;
                }
                this.computeValues();
            }
            return;
        }
        this.minimizeGoal(this.mGoal);
    }

    void minimizeGoal(Row row) throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.minimizeGoal;
            metrics = sMetrics;
            metrics.maxVariables = Math.max(metrics.maxVariables, (long)this.mNumColumns);
            metrics = sMetrics;
            metrics.maxRows = Math.max(metrics.maxRows, (long)this.mNumRows);
        }
        this.updateRowFromVariables((ArrayRow)row);
        this.enforceBFS(row);
        this.optimize(row, false);
        this.computeValues();
    }

    public void reset() {
        int n;
        Object object;
        for (n = 0; n < this.mCache.mIndexedVariables.length; ++n) {
            object = this.mCache.mIndexedVariables[n];
            if (object == null) continue;
            object.reset();
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        object = this.mVariables;
        if (object != null) {
            object.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (n = 0; n < this.mNumRows; ++n) {
            this.mRows[n].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }

    static interface Row {
        public void addError(SolverVariable var1);

        public void clear();

        public SolverVariable getKey();

        public SolverVariable getPivotCandidate(LinearSystem var1, boolean[] var2);

        public void initFromRow(Row var1);

        public boolean isEmpty();
    }

}

