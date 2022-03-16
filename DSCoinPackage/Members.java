package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;
import java.util.Collection;

public class Members {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans = new Transaction[100];
  // public int last_index;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(this.mycoins.get(0).first,
        this.mycoins.get(0).second);
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = p.first;
    tobj.Source = this;
    tobj.coinsrc_block = p.second;
    // tobj.Destinaton
    int i = 0;
    while (i < DSobj.memberlist.length) {
      if (DSobj.memberlist[i].UID.compareTo(destUID) == 0) {
        break;
      }
      i++;
    }
    tobj.Destination = DSobj.memberlist[i];

    int j = 0;
    while (this.in_process_trans[j] != null) {
      j++;
    }
    this.in_process_trans[j] = tobj;
    // this.last_index=j;
    DSobj.pendingTransactions.AddTransactions(tobj);

  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(this.mycoins.get(0).first,
        this.mycoins.get(0).second);
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = p.first;
    tobj.Source = this;
    tobj.coinsrc_block = p.second;
    // tobj.Destinaton
    int i = 0;
    while (i < DSobj.memberlist.length) {
      if (DSobj.memberlist[i].UID.compareTo(destUID) == 0) {
        break;
      }
      i++;
    }
    tobj.Destination = DSobj.memberlist[i];

    int j = 0;
    while (this.in_process_trans[j] != null) {
      j++;
    }
    this.in_process_trans[j] = tobj;
    // this.last_index=j;
    DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend(Transaction tobj,
      DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock tB = DSObj.bChain.lastBlock;
    int f = -1; // to get index
    int i = 0;

    while (tB != null) {
      for (i = 0; i < tB.trarray.length; i++) {
        // if (tB.trarray[i]==tobj) {
        if (tB.trarray[i].equals(tobj)) {
          f = i;
          break;
        }
      }
      if (f == -1) {
        tB = tB.previous;
      } else {
        break;
      }
    }

    if (f == -1) {
      System.out.println("Coin ID: " + tobj.coinID);
      throw new MissingTransactionException();
    }
    List<Pair<String, String>> proof = new ArrayList<Pair<String, String>>();
    List<Pair<String, String>> p = new ArrayList<Pair<String, String>>();

    // last node or leaf node
    TreeNode tmp = tB.Tree.leaf.get(f);

    // First left and then right child val
    while (tmp != tB.Tree.rootnode) {
      if (tmp.parent.left == tmp) {
        proof.add(new Pair<String, String>(tmp.val, tmp.parent.right.val));
      } else {
        proof.add(new Pair<String, String>(tmp.parent.left.val, tmp.val));

      }
      tmp = tmp.parent;
    }
    proof.add(new Pair<String, String>(tmp.val, null));

    TransactionBlock b = DSObj.bChain.lastBlock;
    while (b != tB) {
      p.add(new Pair<String, String>(b.dgst, b.previous.dgst + "#" + b.trsummary + "#" + b.nonce));
      b = b.previous;
    }

    if (b.previous != null) {
      p.add(new Pair<String, String>(b.dgst, b.previous.dgst + "#" + b.trsummary + "#" + b.nonce));
      p.add(new Pair<String, String>(b.previous.dgst, null));

    } else {
      p.add(new Pair<String, String>(b.dgst, "DSCoin" + "#" + b.trsummary + "#" + b.nonce));
      p.add(new Pair<String, String>("DSCoin", null));

    }

    // removing transaction from in process transaction list

    int index = 0; // to find index of current transaction tobj
    int k = 0; // to find length of inprocess transaction
    while (this.in_process_trans[k] != null) {
      if (this.in_process_trans[k].equals(tobj)) {
        index = k;
      }
      k++;
    }
    this.in_process_trans[index] = this.in_process_trans[k - 1]; // placing last transaction in place of tobj
    this.in_process_trans[k - 1] = null; // setting last to null

    Collections.reverse(p);

    // ading this object to destinations mycoin list
    tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, tB));

    // arranging in ascending order
    sorting(tobj.Destination.mycoins);

    return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(proof, p);

  }

  public void MineCoin(DSCoin_Honest DSObj) {
    int count = 0;
    Transaction[] t = new Transaction[DSObj.bChain.tr_count];

    try {
      while (count != DSObj.bChain.tr_count - 1) {
        if (DSObj.bChain.lastBlock.checkTransaction(DSObj.pendingTransactions.firstTransaction)
            && checkDoubleSpending(t, count, DSObj.pendingTransactions.firstTransaction.coinID)) {
          t[count] = DSObj.pendingTransactions.firstTransaction;
          count++;

          DSObj.pendingTransactions.RemoveTransaction();
        } else {

          DSObj.pendingTransactions.RemoveTransaction();
        }
      }
    } catch (Exception e) {
    }
    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.coinID = DSObj.latestCoinID;
    minerRewardTransaction.Source = null;
    minerRewardTransaction.coinsrc_block = null;
    minerRewardTransaction.Destination = this;

    t[count] = minerRewardTransaction;
    TransactionBlock tB = new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Honest(tB);

    this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, tB)); // addind to mcoins

    // increasing latest coinID
    DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);

  }

  public void MineCoin(DSCoin_Malicious DSObj) {
    int count = 0;
    Transaction[] t = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock b = DSObj.bChain.FindLongestValidChain();

    try {
      while (count != DSObj.bChain.tr_count - 1) {
  
        if (b.checkTransaction(DSObj.pendingTransactions.firstTransaction)
            && checkDoubleSpending(t, count, DSObj.pendingTransactions.firstTransaction.coinID)) {
          t[count] = DSObj.pendingTransactions.firstTransaction;
          count++;
          DSObj.pendingTransactions.RemoveTransaction();
        } else {
          DSObj.pendingTransactions.RemoveTransaction();
        }
      }
    } catch (Exception e) {
    }
    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.coinID = DSObj.latestCoinID;
    minerRewardTransaction.Source = null;
    minerRewardTransaction.coinsrc_block = null;
    minerRewardTransaction.Destination = this;
    // minerRewardTransaction.Destination = DSObj.memberlist[i];
    t[count] = minerRewardTransaction;

    TransactionBlock tB = new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Malicious(tB);
    this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, tB)); // addind to mycoins

    // increasing latest coinID
    DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
  }

  public void sorting(List<Pair<String, TransactionBlock>> L) {
    for (int i = 0; i < L.size(); i++) {
      for (int j = 0; j < L.size() - i - 1; j++) {
        if (L.get(j).first.compareTo(L.get(j + 1).first) > 0) {
          Collections.swap(L, j, j + 1);
        }
      }
    }
  }

  public boolean checkDoubleSpending(Transaction[] t, int count, String ID) {
    boolean f = false;

    for (int i = 0; i < count; i++) {
      if (t[i].coinID.compareTo(ID) == 0) {
        f = true;
        break;
      }
    }
    if (f) {
      return false;
    } else {
      return true;
    }

  }
}
