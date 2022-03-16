package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;
public class DriverCode_6_old {

    public static String compute_nonce(String prevdgst, String trsummary) {
        long nonce = 1000000000L;

        CRF obj = new CRF(64);

        while (!obj.Fn(prevdgst + "#" + trsummary + "#" + nonce).substring(0,4).equals("0000")) {
            nonce += 1;
        }

        return Long.toString(nonce);

    }

    public static void main(String args[]) {
        
        CRF obj = new CRF(64);

        int score = 0;

        DSCoin_Malicious DSObj = new DSCoin_Malicious();
        DSObj.pendingTransactions = new TransactionQueue();
        DSObj.bChain = new BlockChain_Malicious();
        DSObj.bChain.tr_count = 2;
        DSObj.bChain.lastBlocksList = new TransactionBlock[100];

        Members m1 = new Members();
        Members m2 = new Members();

        m1.UID = "101";
        m2.UID = "102";

        m1.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m2.mycoins = new ArrayList<Pair<String,TransactionBlock>>();

        m1.in_process_trans = new Transaction[100];
        m2.in_process_trans = new Transaction[100];

        DSObj.memberlist = new Members[2];
        DSObj.memberlist[0] = m1;
        DSObj.memberlist[1] = m2;


        // todo : implement own Moderator instead of using student's Moderator
        // to avoid double penalty
        Moderator mod = new Moderator();
        try {
            mod.initializeDSCoin(DSObj, 8);
        } catch (Exception e) {
            System.out.println(e);
        }

        TransactionBlock b4 = DSObj.bChain.lastBlocksList[0];
        TransactionBlock b3 = b4.previous;
        TransactionBlock b2 = b3.previous;
        TransactionBlock b1 = b2.previous;





        // valid transaction
        Transaction t1 = new Transaction();
        t1.coinID = "100000";
        t1.Source = m1;
        t1.Destination = m2;
        t1.coinsrc_block = b1;

        // Reward transaction
        Transaction t2 = new Transaction();
        t2.coinID = "100008";
        t2.Source = null;
        t2.Destination = m2;
        t2.coinsrc_block = null;

        Transaction[] arr = new Transaction[2];
        arr[0] = t1;
        arr[1] = t2;
        TransactionBlock b5 = new TransactionBlock(arr);

        b5.nonce = compute_nonce(b4.dgst, b5.trsummary);
        b5.dgst = obj.Fn(b4.dgst + "#" + b5.trsummary + "#" + b5.nonce);
        b5.previous = b4;
        




        // valid transaction
        Transaction t3 = new Transaction();
        t3.coinID = "100002";
        t3.Source = m1;
        t3.Destination = m2;
        t3.coinsrc_block = b2;

        // Reward transaction
        Transaction t4 = new Transaction();
        t4.coinID = "100009";
        t4.Source = null;
        t4.Destination = m2;
        t4.coinsrc_block = null;

        Transaction[] arr2 = new Transaction[2];
        arr2[0] = t3;
        arr2[1] = t4;
        TransactionBlock b6 = new TransactionBlock(arr2);

        b6.nonce = compute_nonce(b5.dgst, b6.trsummary);
        b6.dgst = obj.Fn(b5.dgst + "#" + b6.trsummary + "#" + b6.nonce);
        b6.previous = b5;

        // at this point, chain is b1 --- b2 --- b3 --- b4 --- b5 --- b6



        // valid transaction
        Transaction t5 = new Transaction();
        t5.coinID = "100004";
        t5.Source = m1;
        t5.Destination = m2;
        t5.coinsrc_block = b3;

        // Reward transaction
        Transaction t6 = new Transaction();
        t6.coinID = "100010";
        t6.Source = null;
        t6.Destination = m2;
        t6.coinsrc_block = null;

        Transaction[] arr3 = new Transaction[2];
        arr3[0] = t5;
        arr3[1] = t6;
        TransactionBlock b7 = new TransactionBlock(arr3);

        b7.nonce = compute_nonce(b4.dgst, b7.trsummary);
        b7.dgst = obj.Fn(b4.dgst + "#" + b7.trsummary + "#" + b7.nonce);
        b7.previous = b4;



        DSObj.bChain.lastBlocksList[0] = b6;
        DSObj.bChain.lastBlocksList[1] = b7;



        // longest chain ends at b4, other chain is b1 --- b2 --- b5, length 3


        // valid transaction
        Transaction t7 = new Transaction();
        t7.coinID = "100001";
        t7.Source = m2;
        t7.Destination = m1;
        t7.coinsrc_block = b1;

        // Reward transaction
        Transaction t8 = new Transaction();
        t8.coinID = "100011";
        t8.Source = null;
        t8.Destination = m1;
        t8.coinsrc_block = null;

        Transaction[] arr4 = new Transaction[2];
        arr4[0] = t7;
        arr4[1] = t8;

        TransactionBlock b8 = new TransactionBlock(arr4);


        DSObj.bChain.InsertBlock_Malicious(b8);

        if(b8.previous.dgst.equals(b6.dgst)) {
            System.out.println("All valid blocks: picked longest chain");
            score += 4;
        }
        else {
            System.out.println("All valid blocks: did not pick longest chain");            
        }


        if((DSObj.bChain.lastBlocksList[0].nonce.equals(b8.nonce) || DSObj.bChain.lastBlocksList[1].nonce.equals(b8.nonce))) {
            System.out.println("Updated lastBlocksList appropriately");
            score += 3;
        }
        else {
            System.out.println("Either lastBlocksList not updated, or newBlock.previous not set appropriately");
        }


        // chain is b1 ..... b8, and b7.previous is b4


        // making b8 invalid
        b8.trarray[0].coinID = "100015";
        // b6 is now invalid, therefore longest chain ends at b6

        // valid transaction
        Transaction t9 = new Transaction();
        t9.coinID = "100003";
        t9.Source = m2;
        t9.Destination = m1;
        t9.coinsrc_block = b1;

        // Reward transaction
        Transaction t10 = new Transaction();
        t10.coinID = "100012";
        t10.Source = null;
        t10.Destination = m1;
        t10.coinsrc_block = null;

        Transaction[] arr5 = new Transaction[2];
        arr5[0] = t9;
        arr5[1] = t10;

        TransactionBlock b9 = new TransactionBlock(arr5);

        DSObj.bChain.InsertBlock_Malicious(b9);




        if((b9.previous.dgst).equals(b6.dgst)) {
            System.out.println("InsertBlock successful when subset of lastBlocksList are invalid");
            score += 5;
        }
        else {
            System.out.println("InsertBlock unsuccessful when subset of lastBlocksList are invalid");            
        }

        // making b5 invalid
        b5.trarray[0].coinID = "100020";
        // b5 is now invalid. Therefore longest chain ends at b7
        // b1--b2--b3--b4--b7


        // valid transaction
        Transaction t11 = new Transaction();
        t11.coinID = "100005";
        t11.Source = m2;
        t11.Destination = m1;
        t11.coinsrc_block = b1;

        // Reward transaction
        Transaction t12 = new Transaction();
        t12.coinID = "100013";
        t12.Source = null;
        t12.Destination = m1;
        t12.coinsrc_block = null;

        Transaction[] arr6 = new Transaction[2];
        arr6[0] = t11;
        arr6[1] = t12;

        TransactionBlock b10 = new TransactionBlock(arr6);


        System.out.println("b4.dgst: "+b4.dgst);
        System.out.println("b5.dgst: "+b5.dgst);        
        System.out.println("b6.dgst: "+b6.dgst);
        System.out.println("b7.dgst: "+b7.dgst);
        System.out.println("b8.dgst: "+b8.dgst);        
        System.out.println("b9.dgst: "+b9.dgst);  

        System.out.println("Longest chain ends at :"+ DSObj.bChain.FindLongestValidChain().dgst);      

        DSObj.bChain.InsertBlock_Malicious(b10);
        

        if((b10.previous.dgst).equals(b7.dgst)) {
            System.out.println("InsertBlock successful in general case");
            score += 8;
        }
        else {
            System.out.println("InsertBlock unsuccessful in general case");            
        }



        System.out.println(score);

    }
}
