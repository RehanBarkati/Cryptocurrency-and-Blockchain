package DSCoinPackage;

import java.sql.Array;
import java.util.*;

import DSCoinPackage.Members;
import DSCoinPackage.Moderator;
import DSCoinPackage.Transaction;
import DSCoinPackage.TransactionBlock;
import HelperClasses.MerkleTree;
import HelperClasses.Pair;

public class DriverCode_2 {
    public static void main(String[] args){
        double score = 0;

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
        
        
        // Case 2a - test case 

        
        Transaction t21 = new Transaction();
        Transaction t22 = new Transaction();
        Transaction t23 = new Transaction();
        Transaction t24 = new Transaction();
        Transaction t25 = new Transaction();
        Transaction t26 = new Transaction();
        Transaction t27 = new Transaction();
        Transaction t28 = new Transaction();
        Transaction t29 = new Transaction();
        Transaction t210 = new Transaction();
        Transaction t211 = new Transaction();
        Transaction t212 = new Transaction();
        Transaction t213 = new Transaction();
        Transaction t214 = new Transaction();
        Transaction t215 = new Transaction();
        Transaction t216 = new Transaction();
        Transaction t217 = new Transaction();


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



        t25.coinID = "000005";
        t25.Source = m0;
        t25.Destination = m1;
        t25.coinsrc_block = null;

        t26.coinID = "000006";
        t26.Source = m0;
        t26.Destination = m2;
        t26.coinsrc_block = null;
        
        t27.coinID = "000007";
        t27.Source = m0;
        t27.Destination = m3;
        t27.coinsrc_block = null;
        
        t28.coinID = "000008";
        t28.Source = m0;
        t28.Destination = m4;
        t28.coinsrc_block = null;


        arr[0] = t25;
        arr[1] = t26;
        arr[2] = t27;
        arr[3] = t28;
        TransactionBlock trblk2 = new TransactionBlock(arr);
        trblk2.previous = trblk1;

        t29.coinID = "000001";
        t29.Source = m1;
        t29.Destination = m3;
        t29.coinsrc_block = trblk1;

        t210.coinID = "000002";
        t210.Source = m2;
        t210.Destination = m3;
        t210.coinsrc_block = trblk1;


        t211.coinID = "000007";
        t211.Source = m3;
        t211.Destination = m1;
        t211.coinsrc_block = trblk2;
        
        t212.coinID = "000008";
        t212.Source = m4;
        t212.Destination = m2;
        t212.coinsrc_block = trblk2;


        arr[0] = t29;
        arr[1] = t210;
        arr[2] = t211;
        arr[3] = t212;

        TransactionBlock trblk3 = new TransactionBlock(arr);
        trblk3.previous = trblk2;


        t213.coinID = "000005";
        t213.Source = m1;
        t213.Destination = m3;
        t213.coinsrc_block = trblk2;

        t214.coinID = "000006";
        t214.Source = m2;
        t214.Destination = m3;
        t214.coinsrc_block = trblk2;


        t215.coinID = "000003";
        t215.Source = m3;
        t215.Destination = m1;
        t215.coinsrc_block = trblk1;
        
        t216.coinID = "000004";
        t216.Source = m4;
        t216.Destination = m2;
        t216.coinsrc_block = trblk1;


        arr[0] = t213;
        arr[1] = t214;
        arr[2] = t215;
        arr[3] = t216;

        TransactionBlock trblk4 = new TransactionBlock(arr);
        trblk4.previous = trblk3;



        // Case 2a - test case
        try {

            t217.coinID = "000001";
            t217.Source = m3;
            t217.Destination = m2;
            t217.coinsrc_block = trblk2;


            if (trblk3.checkTransaction(t217) == false) {
                score += 0.5;
            } 
            else {
                score += 0;
                System.out.println("score -= 0.5 : coin source block not checked properly");
            }


            t217.coinID = "000004";
            t217.Source = m3;
            t217.Destination = m2;
            t217.coinsrc_block = trblk2;


            if (trblk3.checkTransaction(t217) == false) {
                score += 0.5;
            } 
            else {
                score += 0;
                System.out.println("score -= 0.5 : coin source block not checked properly");
            }
            


            t217.coinID = "000004";
            t217.Source = m4;
            t217.Destination = m2;
            t217.coinsrc_block = trblk1;


            if (trblk3.checkTransaction(t217) == false) {
                score += 0;
                System.out.println("score -= 0.5 : coin source block not checked properly");
            } 
            else {
                score += 0.5;
            }

            t217.coinID = "000001";
            t217.Source = m3;
            t217.Destination = m2;
            t217.coinsrc_block = trblk3;


            if (trblk3.checkTransaction(t217) == false) {
                System.out.println("score -= 0.5 : coin source block not checked properly");
                score += 0;
            } 
            else {
                score += 0.5;
            }
            

        }
        catch (Exception e) {
            score += 0;
        }





        System.out.println("Score after 2a-test: " + score + "/2.0");



        // Case 2b
        try {

            t217.coinID = "000001";
            t217.Source = m1;
            t217.Destination = m2;
            t217.coinsrc_block = trblk1;

            if (trblk4.checkTransaction(t217) == false) {
                score += 4;
            } 
            else {
                System.out.println("score -= 4 : Double spending not detected");
                score += 0;
            }

            t217.coinID = "000002";
            t217.Source = m3;
            t217.Destination = m2;
            t217.coinsrc_block = trblk3;

            if (trblk4.checkTransaction(t217) == false) {
                System.out.println("score -= 4 : No Double Spending in this case");
                score += 0;
            } 
            else {
                score += 4;
            }
        }
        catch (Exception e) {
            score += 0;
        }
        System.out.println("Final Score:");
        System.out.println(score);

    }
}
