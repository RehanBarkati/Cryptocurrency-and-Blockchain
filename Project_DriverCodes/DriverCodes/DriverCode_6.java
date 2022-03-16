package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;
public class DriverCode_6 {

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

        int num_of_members=2, tr_count=2;
        int coinCount = 8;
        String initial_member_id = "101", initial_coin_id  = "100000";


        // Creating moderator object

        Moderator mod = new Moderator();

        // Creating and initialising attributes of DSCoin
        DSCoin_Malicious DSObj = new DSCoin_Malicious();
        DSObj.pendingTransactions = new TransactionQueue();
        DSObj.bChain = new BlockChain_Malicious();
        DSObj.bChain.lastBlocksList = new TransactionBlock[100];


        DSObj.bChain.tr_count = 2;
        DSObj.memberlist = new Members[num_of_members];
        DSObj.latestCoinID = initial_coin_id;

        // Initialising attributes of members of DSCoin
        for(int i=0;i<num_of_members;i++)
        {
            Members m = new Members();
            m.UID = Long.toString(Long.parseLong(initial_member_id) + i);
            m.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
            m.in_process_trans = new Transaction[100];
            DSObj.memberlist[i] = m;
        }

        // Calling initializeDSCoin method
        try
        {
            mod.initializeDSCoin(DSObj, coinCount);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    Members m1 = DSObj.memberlist[0];

    Members m2 = DSObj.memberlist[1];

        TransactionBlock b4 = DSObj.bChain.lastBlocksList[0];
        TransactionBlock b3 = b4.previous;
        TransactionBlock b2 = b3.previous;
        TransactionBlock b1 = b2.previous;


    if(DSObj.bChain.lastBlocksList[0] != null)
        System.out.println("DSObj.bChain.lastBlocksList[0] after mod init : " + DSObj.bChain.lastBlocksList[0].dgst);



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

    DSObj.bChain.InsertBlock_Malicious(b5);



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


    DSObj.bChain.InsertBlock_Malicious(b6);
    
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

    String d = b5.dgst;
    b5.dgst = "1111";
    // b5 invalid now
    
    DSObj.bChain.InsertBlock_Malicious(b7);
    
    

    if(DSObj.bChain.lastBlocksList[0] != null)
        System.out.println("DSObj.bChain.lastBlocksList[0] after b7 insert : " + DSObj.bChain.lastBlocksList[0].dgst);

    if(DSObj.bChain.lastBlocksList[1] != null)
        System.out.println("DSObj.bChain.lastBlocksList[1] after b7 insert : " + DSObj.bChain.lastBlocksList[1].dgst);

    
    b5.dgst = d;
    

        System.out.println("b4.dgst: "+b4.dgst);
        System.out.println("b5.dgst: "+b5.dgst);        
        System.out.println("b6.dgst: "+b6.dgst);
        System.out.println("b7.dgst: "+b7.dgst);

    
        if(DSObj.bChain.lastBlocksList[0] != null) 
            System.out.println("lastBlocksList[0].dgst : " + DSObj.bChain.lastBlocksList[0].dgst);

        if(DSObj.bChain.lastBlocksList[1] != null) 
            System.out.println("lastBlocksList[1].dgst : " + DSObj.bChain.lastBlocksList[1].dgst);

        // longest chain ends at b6, other chain is b1 --- b2 --- b3 --- b4 --- b7, length 5


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
        b5.dgst = "10000";
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

        System.out.println("\n\n\n Longest chain ends at :"+ DSObj.bChain.FindLongestValidChain().dgst);      

    if(DSObj.bChain.lastBlocksList[0] != null) System.out.println("\n\n\n lastBlocksList[0] :" + DSObj.bChain.lastBlocksList[0].dgst); 
    if(DSObj.bChain.lastBlocksList[1] != null) System.out.println("\n\n\n lastBlocksList[1] :" + DSObj.bChain.lastBlocksList[1].dgst); 
    if(DSObj.bChain.lastBlocksList[2] != null) System.out.println("\n\n\n lastBlocksList[2] :" + DSObj.bChain.lastBlocksList[2].dgst); 

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
