package DSCoinPackage;

import java.sql.Array;
import java.util.*;

import DSCoinPackage.Moderator;
import DSCoinPackage.Transaction;
import DSCoinPackage.TransactionBlock;
import DSCoinPackage.Members;
import HelperClasses.MerkleTree;
import HelperClasses.Pair;

public class DriverCode_3 {
    public static void main(String[] args){
        double score = 0.0;

        Transaction[] arr = new Transaction[4];

        Members m0 = new Members();
        Members m1 = new Members();
        Members m2 = new Members();
        Members m3 = new Members();
        Members m4 = new Members();
        
        m0.UID = "Moderator";
        m1.UID = "member1";
        m2.UID = "member2";
        m3.UID = "member3";
        m4.UID = "member4";
        
        Transaction t21 = new Transaction();
        Transaction t22 = new Transaction();
        Transaction t23 = new Transaction();
        Transaction t24 = new Transaction();



        t21.coinID = "000001";
        t21.Source = m0;
        t21.Destination = m1;
        t21.coinsrc_block = null;

        t22.coinID = "000002";
        t22.Source = m0;
        t22.Destination = m2;
        t22.coinsrc_block = null;
        
        t23.coinID = "000003";
        t23.Source = m0;
        t23.Destination = m3;
        t23.coinsrc_block = null;
        
        t24.coinID = "000004";
        t24.Source = m0;
        t24.Destination = m4;
        t24.coinsrc_block = null;

        arr[0] = t21;
        arr[1] = t22;
        arr[2] = t23;
        arr[3] = t24;
        TransactionBlock trblk1 = new TransactionBlock(arr);
        MerkleTree tr1 = new MerkleTree();
        String rootval = tr1.Build(arr);

        try {
            

            t21.coinID = "000000";
            t21.Source = m0;
            t21.Destination = m2;
            t21.coinsrc_block = null;
    
            t22.coinID = "000000";
            t22.Source = m2;
            t22.Destination = m1;
            t22.coinsrc_block = null;

            TransactionBlock trblk = new TransactionBlock(arr);

            if (((trblk1.trarray[0].coinID).equals("000001")) && ((trblk1.trarray[1].coinID).equals("000002"))) {
                score += 5;
            } 
            else {
                score += 0;
                System.out.println("score -= 5 : Updating input array should not change the transaction block's transactions");
            }

            if ((trblk1.trsummary.equals(rootval)))
                score += 10;
            else {
                System.out.println("score -= 10 : Trsummary not matching the tree root value");
            }

        }
        catch (Exception e) {
            score += 0;
        }

        System.out.println("Final score: ");
        System.out.println(score);
        
    }
}
