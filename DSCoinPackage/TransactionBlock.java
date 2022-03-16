package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    this.trarray = new Transaction[t.length];

    for (int i = 0; i < t.length; i++) {
      Transaction tn=new Transaction();
      tn.Destination=t[i].Destination;
      tn.Source=t[i].Source;
      tn.coinID=t[i].coinID;
      tn.coinsrc_block=t[i].coinsrc_block;
      tn.next=t[i].next;
      this.trarray[i]=tn;
    }
    MerkleTree m = new MerkleTree();

    this.trsummary = m.Build(trarray);
    this.Tree = m;
    this.previous = null;
    this.dgst = null;

  }

  public boolean checkTransaction(Transaction t) {

    if (t.coinsrc_block == null) {
      return true;
    }
    TransactionBlock b = this;
    
    boolean flag = false;


    while (b != t.coinsrc_block) {
      for (int i = 0; i < b.trarray.length; i++) {
        if (b.trarray[i].coinID.equals(t.coinID)) {
          flag = true;
          break;
        }
      }
      // double spending is found or not
      if (!flag) {
        b = b.previous;
      } else {
        break;
      }
    }

    if (!flag) {
      for (int i = 0; i < b.trarray.length; i++) {
        if (b.trarray[i].coinID.equals(t.coinID) && b.trarray[i].Destination.equals(t.Source)) {
          return true;
        }
      }
    }

    return false;
  }
}
