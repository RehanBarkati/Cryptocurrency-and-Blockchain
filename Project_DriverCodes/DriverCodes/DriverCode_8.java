package DSCoinPackage;

import HelperClasses.*;
import java.util.*;
public class DriverCode_8 {

    public static void main(String args[]) {

        DSCoin_Honest DSObj = new DSCoin_Honest();
        DSObj.pendingTransactions = new TransactionQueue();
        DSObj.bChain = new BlockChain_Honest();
        DSObj.bChain.tr_count = 4;

        Members m1 = new Members();
        Members m2 = new Members();
        Members m3 = new Members();
        Members m4 = new Members();
        m1.UID = "101";
        m2.UID = "102";
        m3.UID = "103";
        m4.UID = "104";
        m1.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m2.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m3.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m4.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m1.in_process_trans = new Transaction[100];
        m2.in_process_trans = new Transaction[100];
        m3.in_process_trans = new Transaction[100];
        m4.in_process_trans = new Transaction[100];
        
        DSObj.memberlist = new Members[4];
        DSObj.memberlist[0] = m1;
        DSObj.memberlist[1] = m2;
        DSObj.memberlist[2] = m3;
        DSObj.memberlist[3] = m4;

        int score = 0;

        Moderator mod = new Moderator();
        try {
            mod.initializeDSCoin(DSObj, 8);
        } catch (Exception e) {
            System.out.println(e);
        }

        TransactionBlock b2 = DSObj.bChain.lastBlock;
        TransactionBlock b1 = DSObj.bChain.lastBlock.previous;

        if(b1 == null){
            System.out.println("B1 block null");
        }
        
        if(b2 == null){
            System.out.println("B2 block null");
        }

        // valid transaction
        Transaction t1 = new Transaction();
        t1.coinID = "100000";
        t1.Source = m1;
        t1.Destination = m2;
        t1.coinsrc_block = b1;

        // valid transaction
        Transaction t2 = new Transaction();
        t2.coinID = "100004";
        t2.Source = m1;
        t2.Destination = m3;
        t2.coinsrc_block = b2;

        // invalid transaction
        Transaction t3 = new Transaction();
        t3.coinID = "100001";
        t3.Source = m1;
        t3.Destination = m3;
        t3.coinsrc_block = b1;

        // invalid transaction
        Transaction t4 = new Transaction();
        t4.coinID = "100005";
        t4.Source = m2;
        t4.Destination = m3;
        t4.coinsrc_block = b1;

        // valid transaction
        Transaction t5 = new Transaction();
        t5.coinID = "100006";
        t5.Source = m3;
        t5.Destination = m2;
        t5.coinsrc_block = b2;

        DSObj.pendingTransactions.AddTransactions(t1);
        DSObj.pendingTransactions.AddTransactions(t2);
        DSObj.pendingTransactions.AddTransactions(t3);
        DSObj.pendingTransactions.AddTransactions(t4);
        DSObj.pendingTransactions.AddTransactions(t5);

        Long pcoinid =  Long.parseLong(DSObj.latestCoinID);

        try{
            m4.MineCoin(DSObj);
        } catch (Exception e) {

        }

        TransactionBlock b3 = DSObj.bChain.lastBlock;
        if(b3 != b2){
            // New block is created and added to block list associated with block chain
            score += (4+5);
            if(DSObj.pendingTransactions.size() == 0){
                // Successfully removed all transactions from pending transactions list
                score += 2;
                if(b3.dgst.equals("000030A7245B8D872AC96F9254C571BB2226B4996752088790540BE5545AF752")){   
                    // Added all valid transactions and miner reward into latest mined block b3
                    score += 6;
                    if(Long.parseLong(DSObj.latestCoinID) == (pcoinid + 1)){
                        // Incremented latest coin id by 1
                        score += 2;
                        if(m4.mycoins.size() == 3){
                            // New coin is added to my coins list of miner m4
                            score += 1;
                        }else{
                            // New coin is not added to my coins list of miner m4
                            System.out.println("New coin not added to my coins list of miner!");
                        }
                    }else{
                        // Not incremented latest coin id by 1
                        System.out.println("Latest coin value not updated!");
                    }
                }else{
                    // Added some invalid transaction or minor reward is not added into block b3
                    System.out.println("Invalid transactions in last block!");
                }
            }else{
                // Not removed all transactions from pending transactions list
                System.out.println("Pending transaction not processed completely!");
            }
        }else{
            // New block is not added to block list associated with block chain
            System.out.println("New block addition failed!");
        }

        System.out.println("Score for honest setting:\n" + Integer.toString(score));


    }
}
