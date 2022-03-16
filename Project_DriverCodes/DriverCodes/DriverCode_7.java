package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;
public class DriverCode_7 {

    static boolean verifyScp(List<Pair<String, String>> path, String trsummary){
        CRF obj = new CRF(64);
        boolean valid = true;

        Pair<String, String> p = path.get(0);
        for(int i=0; i<path.size()-1; i++){
                String hsh = obj.Fn(p.first + "#" + p.second);
                p = path.get(i+1);
                if(!hsh.equals(p.first) && !hsh.equals(p.second)){
                        valid = false;
                        break;
                }
        }

        if(valid && !p.first.equals(trsummary)){
            valid = false;
        }

        return valid;
    }

    static boolean verifyBl(List<Pair<String, String>> bl){
        CRF obj  = new CRF(64);
        Pair<String, String> p;
        p = bl.get(0);
        String dgst_prev = p.first;

        boolean valid = true;
        for(int i=1; i<bl.size(); i++){
            p = bl.get(i);
            if(!p.second.substring(0, 64).equals(dgst_prev) || !p.first.equals(obj.Fn(p.second))){
                valid = false;
                break;
            } 
            dgst_prev = p.first;
        }

        return valid;
    }
    
    public static <T> int getLength(T[] arr){
        int count = 0;
        for(T el : arr)
            if (el != null)
                ++count;
        return count;
    }

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

        // valid transaction
        m1.initiateCoinsend("102", DSObj);
        Transaction t1 = new Transaction();
        t1.coinID = "100000";
        t1.Source = m1;
        t1.Destination = m2;
        t1.coinsrc_block = b1;

        // valid transaction
        m1.initiateCoinsend("103", DSObj);
        Transaction t2 = new Transaction();
        t2.coinID = "100004";
        t2.Source = m1;
        t2.Destination = m3;
        t2.coinsrc_block = b2;

        // valid transaction
        m3.initiateCoinsend("102", DSObj);
        Transaction t3 = new Transaction();
        t3.coinID = "100002";
        t3.Source = m3;
        t3.Destination = m2;
        t3.coinsrc_block = b1;

        if(m1.mycoins.size() == 0 && m3.mycoins.get(0).first.equals("100006")){
            // Coins removed from the mycoins list
            System.out.println("initiateCoinsend: T1 Passed");
            score += 1;
        }else{
            System.out.println("initiateCoinsend: T1 Failed");
        }

        if(getLength(m1.in_process_trans) == 2 && getLength(m3.in_process_trans) == 1){
            // Added transactions to the in process transaction list
            System.out.println("initiateCoinsend: T2 Passed");
            score += 2;
        }else{
            System.out.println("initiateCoinsend: T2 Failed");
        }

        if(DSObj.pendingTransactions.size() == 3){
            // Added transaction to the pending transaction list associated with block chain
            System.out.println("initiateCoinsend: T3 Passed");
            score += 2;
        }else{
            System.out.println("initiateCoinsend: T3 Failed");
        }

        // Mined block of transactions
        try{
            m4.MineCoin(DSObj);
        } catch (Exception e) {

        }

        TransactionBlock b3 = DSObj.bChain.lastBlock;

        // finalizeCoinsend
        try{
            Pair<List<Pair<String, String>>,List<Pair<String, String>>> fsr1 = m1.finalizeCoinsend(t1, DSObj),
            fsr2 = m1.finalizeCoinsend(t2, DSObj),
            fsr3 = m3.finalizeCoinsend(t3, DSObj);

            if(getLength(m1.in_process_trans) == 0 && getLength(m3.in_process_trans) == 0){
                // Removed transactions from in process transaction of the memebers
                System.out.println("finalizeCoinsend: T1 Passed");
                score += 2;
            }else{
                System.out.println("finalizeCoinsend: T1 Failed");
            }
            
            List<Pair<String, String>> scp1 = fsr1.first,
            scp2 = fsr2.first,
            scp3 = fsr3.first;
            if(verifyScp(scp1, b3.trsummary) && verifyScp(scp2, b3.trsummary) && verifyScp(scp3, b3.trsummary)){
                // Correct sibling coupled path to the root
                System.out.println("finalizeCoinsend: T2 Passed");
                score+=4;
            }else{
                System.out.println("finalizeCoinsend: T2 Failed");
            }

            List<Pair<String, String>> bl1 = fsr1.second,
            bl2 = fsr2.second,
            bl3 = fsr3.second;

            if(verifyBl(bl1) && verifyBl(bl2) && verifyBl(bl3)){
                // Correct list of second pairs
                System.out.println("finalizeCoinsend: T3 Passed");
                score+=4;
            }else{
                System.out.println("finalizeCoinsend: T3 Failed");
            }

        }catch(Exception e){
            System.out.println("finalizeCoinsend: Failed");
        }

        System.out.println("Score for exercise 7: \n"+Integer.toString(score));
    }
}
