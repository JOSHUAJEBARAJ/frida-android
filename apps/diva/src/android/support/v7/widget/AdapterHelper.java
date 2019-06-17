/*
 * Decompiled with CFR 0_121.
 */
package android.support.v7.widget;

import android.support.v4.util.Pools;
import android.support.v7.widget.OpReorderer;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper
implements OpReorderer.Callback {
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes = 0;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates = new ArrayList();
    final ArrayList<UpdateOp> mPostponedList = new ArrayList();
    private Pools.Pool<UpdateOp> mUpdateOpPool = new Pools.SimplePool<UpdateOp>(30);

    AdapterHelper(Callback callback) {
        this(callback, false);
    }

    AdapterHelper(Callback callback, boolean bl) {
        this.mCallback = callback;
        this.mDisableRecycler = bl;
        this.mOpReorderer = new OpReorderer(this);
    }

    private void applyAdd(UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }

    private void applyMove(UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyRemove(UpdateOp updateOp) {
        int n = updateOp.positionStart;
        int n2 = 0;
        int n3 = updateOp.positionStart + updateOp.itemCount;
        int n4 = -1;
        for (int i = updateOp.positionStart; i < n3; ++i) {
            int n5 = 0;
            int n6 = 0;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                if (n4 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, n, n2, null));
                    n6 = 1;
                }
                n4 = 1;
                n5 = n6;
                n6 = n4;
            } else {
                if (n4 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, n, n2, null));
                    n5 = 1;
                }
                n6 = 0;
            }
            if (n5 != 0) {
                i -= n2;
                n3 -= n2;
                n5 = 1;
            } else {
                n5 = n2 + 1;
            }
            n2 = n5;
            n4 = n6;
        }
        UpdateOp updateOp2 = updateOp;
        if (n2 != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            updateOp2 = this.obtainUpdateOp(2, n, n2, null);
        }
        if (n4 == 0) {
            this.dispatchAndUpdateViewHolders(updateOp2);
            return;
        }
        this.postponeAndUpdateViewHolders(updateOp2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyUpdate(UpdateOp updateOp) {
        int n = updateOp.positionStart;
        int n2 = 0;
        int n3 = updateOp.positionStart;
        int n4 = updateOp.itemCount;
        int n5 = -1;
        for (int i = updateOp.positionStart; i < n3 + n4; ++i) {
            int n6;
            int n7;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                n6 = n2;
                int n8 = n;
                if (n5 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, n, n2, updateOp.payload));
                    n6 = 0;
                    n8 = i;
                }
                n7 = 1;
                n = n8;
            } else {
                n6 = n2;
                n7 = n;
                if (n5 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, n, n2, updateOp.payload));
                    n6 = 0;
                    n7 = i;
                }
                n2 = 0;
                n = n7;
                n7 = n2;
            }
            n2 = n6 + 1;
            n5 = n7;
        }
        Object object = updateOp;
        if (n2 != updateOp.itemCount) {
            object = updateOp.payload;
            this.recycleUpdateOp(updateOp);
            object = this.obtainUpdateOp(4, n, n2, object);
        }
        if (n5 == 0) {
            this.dispatchAndUpdateViewHolders((UpdateOp)object);
            return;
        }
        this.postponeAndUpdateViewHolders((UpdateOp)object);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean canFindInPreLayout(int n) {
        int n2 = this.mPostponedList.size();
        int n3 = 0;
        while (n3 < n2) {
            UpdateOp updateOp = this.mPostponedList.get(n3);
            if (updateOp.cmd == 8) {
                if (this.findPositionOffset(updateOp.itemCount, n3 + 1) == n) {
                    return true;
                }
            } else if (updateOp.cmd == 1) {
                int n4 = updateOp.positionStart;
                int n5 = updateOp.itemCount;
                for (int i = updateOp.positionStart; i < n4 + n5; ++i) {
                    if (this.findPositionOffset(i, n3 + 1) == n) return true;
                }
            }
            ++n3;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void dispatchAndUpdateViewHolders(UpdateOp updateOp) {
        Object object;
        int n;
        if (updateOp.cmd == 1 || updateOp.cmd == 8) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        int n2 = this.updatePositionWithPostponed(updateOp.positionStart, updateOp.cmd);
        int n3 = 1;
        int n4 = updateOp.positionStart;
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("op should be remove or update." + updateOp);
            }
            case 4: {
                n = 1;
                break;
            }
            case 2: {
                n = 0;
                break;
            }
        }
        for (int i = 1; i < updateOp.itemCount; ++i) {
            int n5;
            int n6 = this.updatePositionWithPostponed(updateOp.positionStart + n * i, updateOp.cmd);
            int n7 = n5 = 0;
            switch (updateOp.cmd) {
                default: {
                    n7 = n5;
                    break;
                }
                case 4: {
                    if (n6 == n2 + 1) {
                        n7 = 1;
                        break;
                    }
                    n7 = 0;
                    break;
                }
                case 2: {
                    if (n6 == n2) {
                        n7 = 1;
                        break;
                    }
                    n7 = 0;
                }
                case 3: 
            }
            if (n7 != 0) {
                n7 = n3 + 1;
            } else {
                object = this.obtainUpdateOp(updateOp.cmd, n2, n3, updateOp.payload);
                this.dispatchFirstPassAndUpdateViewHolders((UpdateOp)object, n4);
                this.recycleUpdateOp((UpdateOp)object);
                n7 = n4;
                if (updateOp.cmd == 4) {
                    n7 = n4 + n3;
                }
                n2 = n6;
                n3 = 1;
                n4 = n7;
                n7 = n3;
            }
            n3 = n7;
        }
        object = updateOp.payload;
        this.recycleUpdateOp(updateOp);
        if (n3 > 0) {
            updateOp = this.obtainUpdateOp(updateOp.cmd, n2, n3, object);
            this.dispatchFirstPassAndUpdateViewHolders(updateOp, n4);
            this.recycleUpdateOp(updateOp);
        }
    }

    private void postponeAndUpdateViewHolders(UpdateOp updateOp) {
        this.mPostponedList.add(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("Unknown update op type for " + updateOp);
            }
            case 1: {
                this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                return;
            }
            case 8: {
                this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                return;
            }
            case 2: {
                this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(updateOp.positionStart, updateOp.itemCount);
                return;
            }
            case 4: 
        }
        this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int updatePositionWithPostponed(int n, int n2) {
        UpdateOp updateOp;
        int n3 = n;
        for (int i = this.mPostponedList.size() - 1; i >= 0; --i) {
            updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                int n4;
                if (updateOp.positionStart < updateOp.itemCount) {
                    n4 = updateOp.positionStart;
                    n = updateOp.itemCount;
                } else {
                    n4 = updateOp.itemCount;
                    n = updateOp.positionStart;
                }
                if (n3 >= n4 && n3 <= n) {
                    if (n4 == updateOp.positionStart) {
                        if (n2 == 1) {
                            ++updateOp.itemCount;
                        } else if (n2 == 2) {
                            --updateOp.itemCount;
                        }
                        n = n3 + 1;
                    } else {
                        if (n2 == 1) {
                            ++updateOp.positionStart;
                        } else if (n2 == 2) {
                            --updateOp.positionStart;
                        }
                        n = n3 - 1;
                    }
                } else {
                    n = n3;
                    if (n3 < updateOp.positionStart) {
                        if (n2 == 1) {
                            ++updateOp.positionStart;
                            ++updateOp.itemCount;
                            n = n3;
                        } else {
                            n = n3;
                            if (n2 == 2) {
                                --updateOp.positionStart;
                                --updateOp.itemCount;
                                n = n3;
                            }
                        }
                    }
                }
            } else if (updateOp.positionStart <= n3) {
                if (updateOp.cmd == 1) {
                    n = n3 - updateOp.itemCount;
                } else {
                    n = n3;
                    if (updateOp.cmd == 2) {
                        n = n3 + updateOp.itemCount;
                    }
                }
            } else if (n2 == 1) {
                ++updateOp.positionStart;
                n = n3;
            } else {
                n = n3;
                if (n2 == 2) {
                    --updateOp.positionStart;
                    n = n3;
                }
            }
            n3 = n;
        }
        n = this.mPostponedList.size() - 1;
        while (n >= 0) {
            updateOp = this.mPostponedList.get(n);
            if (updateOp.cmd == 8) {
                if (updateOp.itemCount == updateOp.positionStart || updateOp.itemCount < 0) {
                    this.mPostponedList.remove(n);
                    this.recycleUpdateOp(updateOp);
                }
            } else if (updateOp.itemCount <= 0) {
                this.mPostponedList.remove(n);
                this.recycleUpdateOp(updateOp);
            }
            --n;
        }
        return n3;
    }

    /* varargs */ AdapterHelper addUpdateOp(UpdateOp ... arrupdateOp) {
        Collections.addAll(this.mPendingUpdates, arrupdateOp);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int applyPendingUpdatesToPosition(int n) {
        int n2 = this.mPendingUpdates.size();
        int n3 = 0;
        int n4 = n;
        do {
            n = n4;
            if (n3 >= n2) return n;
            UpdateOp updateOp = this.mPendingUpdates.get(n3);
            switch (updateOp.cmd) {
                default: {
                    n = n4;
                    break;
                }
                case 1: {
                    n = n4;
                    if (updateOp.positionStart > n4) break;
                    n = n4 + updateOp.itemCount;
                    break;
                }
                case 2: {
                    n = n4;
                    if (updateOp.positionStart > n4) break;
                    if (updateOp.positionStart + updateOp.itemCount > n4) {
                        return -1;
                    }
                    n = n4 - updateOp.itemCount;
                    break;
                }
                case 8: {
                    if (updateOp.positionStart == n4) {
                        n = updateOp.itemCount;
                        break;
                    }
                    int n5 = n4;
                    if (updateOp.positionStart < n4) {
                        n5 = n4 - 1;
                    }
                    n = n5;
                    if (updateOp.itemCount > n5) break;
                    n = n5 + 1;
                }
            }
            ++n3;
            n4 = n;
        } while (true);
    }

    void consumePostponedUpdates() {
        int n = this.mPostponedList.size();
        for (int i = 0; i < n; ++i) {
            this.mCallback.onDispatchSecondPass(this.mPostponedList.get(i));
        }
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void consumeUpdatesInOnePass() {
        this.consumePostponedUpdates();
        var2_1 = this.mPendingUpdates.size();
        var1_2 = 0;
        do {
            if (var1_2 >= var2_1) {
                this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
                this.mExistingUpdateTypes = 0;
                return;
            }
            var3_3 = this.mPendingUpdates.get(var1_2);
            switch (var3_3.cmd) {
                case 1: {
                    this.mCallback.onDispatchSecondPass(var3_3);
                    this.mCallback.offsetPositionsForAdd(var3_3.positionStart, var3_3.itemCount);
                    ** break;
                }
                case 2: {
                    this.mCallback.onDispatchSecondPass(var3_3);
                    this.mCallback.offsetPositionsForRemovingInvisible(var3_3.positionStart, var3_3.itemCount);
                    ** break;
                }
                case 4: {
                    this.mCallback.onDispatchSecondPass(var3_3);
                    this.mCallback.markViewHoldersUpdated(var3_3.positionStart, var3_3.itemCount, var3_3.payload);
                }
lbl22: // 4 sources:
                default: {
                    ** GOTO lbl27
                }
                case 8: 
            }
            this.mCallback.onDispatchSecondPass(var3_3);
            this.mCallback.offsetPositionsForMove(var3_3.positionStart, var3_3.itemCount);
lbl27: // 2 sources:
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
            ++var1_2;
        } while (true);
    }

    void dispatchFirstPassAndUpdateViewHolders(UpdateOp updateOp, int n) {
        this.mCallback.onDispatchFirstPass(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
            }
            case 2: {
                this.mCallback.offsetPositionsForRemovingInvisible(n, updateOp.itemCount);
                return;
            }
            case 4: 
        }
        this.mCallback.markViewHoldersUpdated(n, updateOp.itemCount, updateOp.payload);
    }

    int findPositionOffset(int n) {
        return this.findPositionOffset(n, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    int findPositionOffset(int n, int n2) {
        int n3 = this.mPostponedList.size();
        int n4 = n2;
        n2 = n;
        do {
            n = n2;
            if (n4 >= n3) return n;
            UpdateOp updateOp = this.mPostponedList.get(n4);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart == n2) {
                    n = updateOp.itemCount;
                } else {
                    int n5 = n2;
                    if (updateOp.positionStart < n2) {
                        n5 = n2 - 1;
                    }
                    n = n5;
                    if (updateOp.itemCount <= n5) {
                        n = n5 + 1;
                    }
                }
            } else {
                n = n2;
                if (updateOp.positionStart <= n2) {
                    if (updateOp.cmd == 2) {
                        if (n2 < updateOp.positionStart + updateOp.itemCount) {
                            return -1;
                        }
                        n = n2 - updateOp.itemCount;
                    } else {
                        n = n2;
                        if (updateOp.cmd == 1) {
                            n = n2 + updateOp.itemCount;
                        }
                    }
                }
            }
            ++n4;
            n2 = n;
        } while (true);
    }

    boolean hasAnyUpdateTypes(int n) {
        if ((this.mExistingUpdateTypes & n) != 0) {
            return true;
        }
        return false;
    }

    boolean hasPendingUpdates() {
        if (this.mPendingUpdates.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public UpdateOp obtainUpdateOp(int n, int n2, int n3, Object object) {
        UpdateOp updateOp = this.mUpdateOpPool.acquire();
        if (updateOp == null) {
            return new UpdateOp(n, n2, n3, object);
        }
        updateOp.cmd = n;
        updateOp.positionStart = n2;
        updateOp.itemCount = n3;
        updateOp.payload = object;
        return updateOp;
    }

    boolean onItemRangeChanged(int n, int n2, Object object) {
        this.mPendingUpdates.add(this.obtainUpdateOp(4, n, n2, object));
        this.mExistingUpdateTypes |= 4;
        if (this.mPendingUpdates.size() == 1) {
            return true;
        }
        return false;
    }

    boolean onItemRangeInserted(int n, int n2) {
        this.mPendingUpdates.add(this.obtainUpdateOp(1, n, n2, null));
        this.mExistingUpdateTypes |= 1;
        if (this.mPendingUpdates.size() == 1) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    boolean onItemRangeMoved(int n, int n2, int n3) {
        boolean bl = true;
        if (n == n2) {
            return false;
        }
        if (n3 != 1) {
            throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(8, n, n2, null));
        this.mExistingUpdateTypes |= 8;
        if (this.mPendingUpdates.size() != 1) return false;
        return bl;
    }

    boolean onItemRangeRemoved(int n, int n2) {
        this.mPendingUpdates.add(this.obtainUpdateOp(2, n, n2, null));
        this.mExistingUpdateTypes |= 2;
        if (this.mPendingUpdates.size() == 1) {
            return true;
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        var2_1 = this.mPendingUpdates.size();
        var1_2 = 0;
        do {
            if (var1_2 >= var2_1) {
                this.mPendingUpdates.clear();
                return;
            }
            var3_3 = this.mPendingUpdates.get(var1_2);
            switch (var3_3.cmd) {
                case 1: {
                    this.applyAdd(var3_3);
                    ** break;
                }
                case 2: {
                    this.applyRemove(var3_3);
                    ** break;
                }
                case 4: {
                    this.applyUpdate(var3_3);
                }
lbl18: // 4 sources:
                default: {
                    ** GOTO lbl22
                }
                case 8: 
            }
            this.applyMove(var3_3);
lbl22: // 2 sources:
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
            ++var1_2;
        } while (true);
    }

    @Override
    public void recycleUpdateOp(UpdateOp updateOp) {
        if (!this.mDisableRecycler) {
            updateOp.payload = null;
            this.mUpdateOpPool.release(updateOp);
        }
    }

    void recycleUpdateOpsAndClearList(List<UpdateOp> list) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            this.recycleUpdateOp(list.get(i));
        }
        list.clear();
    }

    void reset() {
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    static interface Callback {
        public RecyclerView.ViewHolder findViewHolder(int var1);

        public void markViewHoldersUpdated(int var1, int var2, Object var3);

        public void offsetPositionsForAdd(int var1, int var2);

        public void offsetPositionsForMove(int var1, int var2);

        public void offsetPositionsForRemovingInvisible(int var1, int var2);

        public void offsetPositionsForRemovingLaidOutOrNewView(int var1, int var2);

        public void onDispatchFirstPass(UpdateOp var1);

        public void onDispatchSecondPass(UpdateOp var1);
    }

    static class UpdateOp {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;

        UpdateOp(int n, int n2, int n3, Object object) {
            this.cmd = n;
            this.positionStart = n2;
            this.itemCount = n3;
            this.payload = object;
        }

        String cmdToString() {
            switch (this.cmd) {
                default: {
                    return "??";
                }
                case 1: {
                    return "add";
                }
                case 2: {
                    return "rm";
                }
                case 4: {
                    return "up";
                }
                case 8: 
            }
            return "mv";
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            object = (UpdateOp)object;
            if (this.cmd != object.cmd) {
                return false;
            }
            if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == object.positionStart && this.positionStart == object.itemCount) return true;
            if (this.itemCount != object.itemCount) {
                return false;
            }
            if (this.positionStart != object.positionStart) {
                return false;
            }
            if (this.payload != null) {
                if (this.payload.equals(object.payload)) return true;
                return false;
            }
            if (object.payload != null) return false;
            return true;
        }

        public int hashCode() {
            return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
        }

        public String toString() {
            return Integer.toHexString(System.identityHashCode(this)) + "[" + this.cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
        }
    }

}

