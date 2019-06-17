/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    private static final int NONE = -1;
    private int ROW_SIZE = 8;
    private SolverVariable candidate = null;
    int currentSize = 0;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private final Cache mCache;
    private boolean mDidFillOnce;
    private int mHead;
    private int mLast;
    private final ArrayRow mRow;

    ArrayLinkedVariables(ArrayRow arrayRow, Cache cache) {
        int n = this.ROW_SIZE;
        this.mArrayIndices = new int[n];
        this.mArrayNextIndices = new int[n];
        this.mArrayValues = new float[n];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = arrayRow;
        this.mCache = cache;
    }

    private boolean isNew(SolverVariable solverVariable, LinearSystem linearSystem) {
        if (solverVariable.usageInRowCount <= 1) {
            return true;
        }
        return false;
    }

    final void add(SolverVariable arrn, float f, boolean bl) {
        int[] arrn2;
        int n;
        if (f == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            float[] arrf = this.mArrayValues;
            int n2 = this.mHead;
            arrf[n2] = f;
            this.mArrayIndices[n2] = arrn.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++arrn.usageInRowCount;
            arrn.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                n2 = this.mLast;
                arrn = this.mArrayIndices;
                if (n2 >= arrn.length) {
                    this.mDidFillOnce = true;
                    this.mLast = arrn.length - 1;
                }
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            if (this.mArrayIndices[n3] == arrn.id) {
                float[] arrf = this.mArrayValues;
                arrf[n3] = arrf[n3] + f;
                if (arrf[n3] == 0.0f) {
                    if (n3 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[n3];
                    } else {
                        arrf = this.mArrayNextIndices;
                        arrf[n4] = arrf[n3];
                    }
                    if (bl) {
                        arrn.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = n3;
                    }
                    --arrn.usageInRowCount;
                    --this.currentSize;
                }
                return;
            }
            if (this.mArrayIndices[n3] < arrn.id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            arrn2 = this.mArrayIndices;
            n3 = arrn2[n] == -1 ? this.mLast : arrn2.length;
        }
        arrn2 = this.mArrayIndices;
        n = n3;
        if (n3 >= arrn2.length) {
            n = n3;
            if (this.currentSize < arrn2.length) {
                int n5 = 0;
                do {
                    arrn2 = this.mArrayIndices;
                    n = n3;
                    if (n5 >= arrn2.length) break;
                    if (arrn2[n5] == -1) {
                        n = n5;
                        break;
                    }
                    ++n5;
                } while (true);
            }
        }
        arrn2 = this.mArrayIndices;
        n3 = n;
        if (n >= arrn2.length) {
            n3 = arrn2.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = arrn.id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            arrn2 = this.mArrayNextIndices;
            arrn2[n3] = arrn2[n4];
            arrn2[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++arrn.usageInRowCount;
        arrn.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if ((n3 = this.mLast) >= (arrn = this.mArrayIndices).length) {
            this.mDidFillOnce = true;
            this.mLast = arrn.length - 1;
        }
    }

    SolverVariable chooseSubject(LinearSystem linearSystem) {
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        float f = 0.0f;
        float f2 = 0.0f;
        boolean bl = false;
        boolean bl2 = false;
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float f3;
            float f4 = this.mArrayValues[n];
            SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (f4 < 0.0f) {
                f3 = f4;
                if (f4 > - 0.001f) {
                    this.mArrayValues[n] = 0.0f;
                    f3 = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
            } else {
                f3 = f4;
                if (f4 < 0.001f) {
                    this.mArrayValues[n] = 0.0f;
                    f3 = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
            }
            SolverVariable solverVariable4 = solverVariable;
            SolverVariable solverVariable5 = solverVariable2;
            f4 = f;
            float f5 = f2;
            boolean bl3 = bl;
            boolean bl4 = bl2;
            if (f3 != 0.0f) {
                if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
                    if (solverVariable2 == null) {
                        solverVariable5 = solverVariable3;
                        bl3 = this.isNew(solverVariable3, linearSystem);
                        solverVariable4 = solverVariable;
                        f4 = f3;
                        f5 = f2;
                        bl4 = bl2;
                    } else if (f > f3) {
                        solverVariable5 = solverVariable3;
                        bl3 = this.isNew(solverVariable3, linearSystem);
                        solverVariable4 = solverVariable;
                        f4 = f3;
                        f5 = f2;
                        bl4 = bl2;
                    } else {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        f4 = f;
                        f5 = f2;
                        bl3 = bl;
                        bl4 = bl2;
                        if (!bl) {
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable2;
                            f4 = f;
                            f5 = f2;
                            bl3 = bl;
                            bl4 = bl2;
                            if (this.isNew(solverVariable3, linearSystem)) {
                                bl3 = true;
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable3;
                                f4 = f3;
                                f5 = f2;
                                bl4 = bl2;
                            }
                        }
                    }
                } else {
                    solverVariable4 = solverVariable;
                    solverVariable5 = solverVariable2;
                    f4 = f;
                    f5 = f2;
                    bl3 = bl;
                    bl4 = bl2;
                    if (solverVariable2 == null) {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        f4 = f;
                        f5 = f2;
                        bl3 = bl;
                        bl4 = bl2;
                        if (f3 < 0.0f) {
                            if (solverVariable == null) {
                                solverVariable4 = solverVariable3;
                                bl4 = this.isNew(solverVariable3, linearSystem);
                                solverVariable5 = solverVariable2;
                                f4 = f;
                                f5 = f3;
                                bl3 = bl;
                            } else if (f2 > f3) {
                                solverVariable4 = solverVariable3;
                                bl4 = this.isNew(solverVariable3, linearSystem);
                                solverVariable5 = solverVariable2;
                                f4 = f;
                                f5 = f3;
                                bl3 = bl;
                            } else {
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable2;
                                f4 = f;
                                f5 = f2;
                                bl3 = bl;
                                bl4 = bl2;
                                if (!bl2) {
                                    solverVariable4 = solverVariable;
                                    solverVariable5 = solverVariable2;
                                    f4 = f;
                                    f5 = f2;
                                    bl3 = bl;
                                    bl4 = bl2;
                                    if (this.isNew(solverVariable3, linearSystem)) {
                                        bl4 = true;
                                        bl3 = bl;
                                        f5 = f3;
                                        f4 = f;
                                        solverVariable5 = solverVariable2;
                                        solverVariable4 = solverVariable3;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            n = this.mArrayNextIndices[n];
            solverVariable = solverVariable4;
            solverVariable2 = solverVariable5;
            f = f4;
            f2 = f5;
            bl = bl3;
            bl2 = bl4;
        }
        if (solverVariable2 != null) {
            return solverVariable2;
        }
        return solverVariable;
    }

    public final void clear() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
            n = this.mArrayNextIndices[n];
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }

    final boolean containsKey(SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    public void display() {
        int n = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable = this.getVariable(i);
            if (solverVariable == null) continue;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(solverVariable);
            stringBuilder.append(" = ");
            stringBuilder.append(this.getVariableValue(i));
            stringBuilder.append(" ");
            printStream.print(stringBuilder.toString());
        }
        System.out.println(" }");
    }

    void divideByAmount(float f) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] arrf = this.mArrayValues;
            arrf[n] = arrf[n] / f;
            n = this.mArrayNextIndices[n];
        }
    }

    public final float get(SolverVariable solverVariable) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return this.mArrayValues[n];
            }
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    SolverVariable getPivotCandidate() {
        var3_1 = this.candidate;
        if (var3_1 != null) return var3_1;
        var2_2 = this.mHead;
        var1_3 = 0;
        var3_1 = null;
        while (var2_2 != -1) {
            if (var1_3 >= this.currentSize) return var3_1;
            var4_4 = var3_1;
            if (this.mArrayValues[var2_2] >= 0.0f) ** GOTO lbl15
            var5_5 = this.mCache.mIndexedVariables[this.mArrayIndices[var2_2]];
            if (var3_1 == null) ** GOTO lbl-1000
            var4_4 = var3_1;
            if (var3_1.strength < var5_5.strength) lbl-1000: // 2 sources:
            {
                var4_4 = var5_5;
            }
lbl15: // 4 sources:
            var2_2 = this.mArrayNextIndices[var2_2];
            ++var1_3;
            var3_1 = var4_4;
        }
        return var3_1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    SolverVariable getPivotCandidate(boolean[] var1_1, SolverVariable var2_2) {
        var7_3 = this.mHead;
        var6_4 = 0;
        var8_5 = null;
        var3_6 = 0.0f;
        while (var7_3 != -1) {
            if (var6_4 >= this.currentSize) return var8_5;
            var9_9 = var8_5;
            var4_7 = var3_6;
            if (this.mArrayValues[var7_3] >= 0.0f) ** GOTO lbl28
            var10_10 = this.mCache.mIndexedVariables[this.mArrayIndices[var7_3]];
            if (var1_1 == null) ** GOTO lbl15
            var9_9 = var8_5;
            var4_7 = var3_6;
            if (var1_1[var10_10.id]) ** GOTO lbl28
lbl15: // 2 sources:
            var9_9 = var8_5;
            var4_7 = var3_6;
            if (var10_10 == var2_2) ** GOTO lbl28
            if (var10_10.mType == SolverVariable.Type.SLACK) ** GOTO lbl-1000
            var9_9 = var8_5;
            var4_7 = var3_6;
            if (var10_10.mType == SolverVariable.Type.ERROR) lbl-1000: // 2 sources:
            {
                var5_8 = this.mArrayValues[var7_3];
                var9_9 = var8_5;
                var4_7 = var3_6;
                if (var5_8 < var3_6) {
                    var4_7 = var5_8;
                    var9_9 = var10_10;
                }
            }
lbl28: // 8 sources:
            var7_3 = this.mArrayNextIndices[var7_3];
            ++var6_4;
            var8_5 = var9_9;
            var3_6 = var4_7;
        }
        return var8_5;
    }

    final SolverVariable getVariable(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[n2]];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return null;
    }

    final float getVariableValue(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mArrayValues[n2];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return 0.0f;
    }

    boolean hasAtLeastOnePositiveVariable() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayValues[n] > 0.0f) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    void invert() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] arrf = this.mArrayValues;
            arrf[n] = arrf[n] * -1.0f;
            n = this.mArrayNextIndices[n];
        }
    }

    public final void put(SolverVariable arrn, float f) {
        int[] arrn2;
        int n;
        if (f == 0.0f) {
            this.remove((SolverVariable)arrn, true);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            float[] arrf = this.mArrayValues;
            int n2 = this.mHead;
            arrf[n2] = f;
            this.mArrayIndices[n2] = arrn.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++arrn.usageInRowCount;
            arrn.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                n2 = this.mLast;
                arrn = this.mArrayIndices;
                if (n2 >= arrn.length) {
                    this.mDidFillOnce = true;
                    this.mLast = arrn.length - 1;
                }
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            if (this.mArrayIndices[n3] == arrn.id) {
                this.mArrayValues[n3] = f;
                return;
            }
            if (this.mArrayIndices[n3] < arrn.id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            arrn2 = this.mArrayIndices;
            n3 = arrn2[n] == -1 ? this.mLast : arrn2.length;
        }
        arrn2 = this.mArrayIndices;
        n = n3;
        if (n3 >= arrn2.length) {
            n = n3;
            if (this.currentSize < arrn2.length) {
                int n5 = 0;
                do {
                    arrn2 = this.mArrayIndices;
                    n = n3;
                    if (n5 >= arrn2.length) break;
                    if (arrn2[n5] == -1) {
                        n = n5;
                        break;
                    }
                    ++n5;
                } while (true);
            }
        }
        arrn2 = this.mArrayIndices;
        n3 = n;
        if (n >= arrn2.length) {
            n3 = arrn2.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = arrn.id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            arrn2 = this.mArrayNextIndices;
            arrn2[n3] = arrn2[n4];
            arrn2[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++arrn.usageInRowCount;
        arrn.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
        }
        if ((n3 = this.mLast) >= (arrn = this.mArrayIndices).length) {
            this.mDidFillOnce = true;
            this.mLast = arrn.length - 1;
        }
    }

    public final float remove(SolverVariable solverVariable, boolean bl) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int n = this.mHead;
        int n2 = -1;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                if (n == this.mHead) {
                    this.mHead = this.mArrayNextIndices[n];
                } else {
                    int[] arrn = this.mArrayNextIndices;
                    arrn[n2] = arrn[n];
                }
                if (bl) {
                    solverVariable.removeFromRow(this.mRow);
                }
                --solverVariable.usageInRowCount;
                --this.currentSize;
                this.mArrayIndices[n] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = n;
                }
                return this.mArrayValues[n];
            }
            n2 = n;
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }

    public String toString() {
        String string2 = "";
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(" -> ");
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mArrayValues[n]);
            stringBuilder.append(" : ");
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mCache.mIndexedVariables[this.mArrayIndices[n]]);
            string2 = stringBuilder.toString();
            n = this.mArrayNextIndices[n];
        }
        return string2;
    }

    final void updateFromRow(ArrayRow arrayRow, ArrayRow arrayRow2, boolean bl) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                float f = this.mArrayValues[n];
                this.remove(arrayRow2.variable, bl);
                ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                n2 = arrayLinkedVariables.mHead;
                for (n = 0; n2 != -1 && n < arrayLinkedVariables.currentSize; ++n) {
                    this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[n2]], arrayLinkedVariables.mArrayValues[n2] * f, bl);
                    n2 = arrayLinkedVariables.mArrayNextIndices[n2];
                }
                arrayRow.constantValue += arrayRow2.constantValue * f;
                if (bl) {
                    arrayRow2.variable.removeFromRow(arrayRow);
                }
                n = this.mHead;
                n2 = 0;
                continue;
            }
            n = this.mArrayNextIndices[n];
            ++n2;
        }
    }

    void updateFromSystem(ArrayRow arrayRow, ArrayRow[] arrarrayRow) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            Object object = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (object.definitionId != -1) {
                float f = this.mArrayValues[n];
                this.remove((SolverVariable)object, true);
                object = arrarrayRow[object.definitionId];
                if (!object.isSimpleDefinition) {
                    ArrayLinkedVariables arrayLinkedVariables = object.variables;
                    n2 = arrayLinkedVariables.mHead;
                    for (n = 0; n2 != -1 && n < arrayLinkedVariables.currentSize; ++n) {
                        this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[n2]], arrayLinkedVariables.mArrayValues[n2] * f, true);
                        n2 = arrayLinkedVariables.mArrayNextIndices[n2];
                    }
                }
                arrayRow.constantValue += object.constantValue * f;
                object.variable.removeFromRow(arrayRow);
                n = this.mHead;
                n2 = 0;
                continue;
            }
            n = this.mArrayNextIndices[n];
            ++n2;
        }
    }
}

