package DSCoinPackage;

import java.util.ArrayList;

import HelperClasses.Pair;

public class Moderator {
  // m.UID="Moderator";

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    Members m = new Members();
    m.UID = "Moderator";
    DSObj.latestCoinID = "100000";
    int size = DSObj.memberlist.length;

    int k = 0; // to count number of coins
    int i=0;
    for (int blocks = 0; blocks < coinCount / DSObj.bChain.tr_count; blocks++) {
      ArrayList<String> coin = new ArrayList<String>();
      Transaction t[] = new Transaction[DSObj.bChain.tr_count];
      int c = 0; // as index for t
      while (c < DSObj.bChain.tr_count) {
        Transaction tr = new Transaction();
        tr.coinID = DSObj.latestCoinID;
        tr.Source = m;
        tr.Destination = DSObj.memberlist[k % size];
        tr.coinsrc_block = null;

        coin.add(DSObj.latestCoinID);
        // DSObj.memberlist[k % size].mycoins.add(new Pair<String,
        // TransactionBlock>(tr.coinID, null));

        t[c] = tr;
        k++;
        c++;
        DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);

      }

      TransactionBlock tB = new TransactionBlock(t);
      DSObj.bChain.InsertBlock_Honest(tB);

      while (i < k) {
        DSObj.memberlist[i % size].mycoins.add(new Pair<String, TransactionBlock>(coin.get(i % c), tB));
        i++;
      }

    }
  }

  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    Members m = new Members();
    m.UID = "Moderator";
    DSObj.latestCoinID = "100000";
    int size = DSObj.memberlist.length;

    int k = 0; // to count number of coins
    int i = k;
    for (int blocks = 0; blocks < coinCount / DSObj.bChain.tr_count; blocks++) {
      ArrayList<String> coin = new ArrayList<String>();
      Transaction t[] = new Transaction[DSObj.bChain.tr_count];
      int c = 0; // as index for t
      while (c < DSObj.bChain.tr_count) {
        Transaction tr = new Transaction();
        tr.coinID = DSObj.latestCoinID;
        tr.Source = m;
        tr.Destination = DSObj.memberlist[k % size];
        tr.coinsrc_block = null;

        coin.add(DSObj.latestCoinID);
        // DSObj.memberlist[k % size].mycoins.add(new Pair<String,
        // TransactionBlock>(tr.coinID, null));

        t[c] = tr;
        k++;
        c++;
        DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);

      }

      TransactionBlock tB = new TransactionBlock(t);
      DSObj.bChain.InsertBlock_Malicious(tB);

      while (i < k) {
        DSObj.memberlist[i % size].mycoins.add(new Pair<String, TransactionBlock>(coin.get(i % c), tB));
        i++;
      }

    }

    // DSObj.bChain.lastBlocksList[0]=tB; is already being updated in
    // insert_malicious

  }
}
