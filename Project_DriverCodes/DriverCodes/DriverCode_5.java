package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;

public class DriverCode_5{
	public static void main(String args[]){
		int score = 0;

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
        

        DSCoin_Malicious obj = new DSCoin_Malicious();
        obj.memberlist = new Members[4];
        obj.memberlist[0] = m1;
        obj.memberlist[1] = m2;
        obj.memberlist[2] = m3;
        obj.memberlist[3] = m4;

        obj.pendingTransactions = new TransactionQueue();
        obj.bChain = new BlockChain_Malicious();
        obj.bChain.tr_count = 4;
        
	obj.bChain.lastBlocksList = new TransactionBlock[100];


        Moderator mod = new Moderator();

		try{
			mod.initializeDSCoin(obj,8);
		}
		catch(Exception e){
			System.out.println(e);
		}

		TransactionBlock tb1 = obj.bChain.lastBlocksList[0].previous;
		TransactionBlock tb2 = obj.bChain.lastBlocksList[0];


		Transaction T1 = new Transaction();
		Transaction T2 = new Transaction();
		Transaction T3 = new Transaction();

		T1.coinID = "100000";
		T1.Source = obj.memberlist[0];
		T1.Destination = obj.memberlist[1];
		T1.coinsrc_block = tb1;
		obj.pendingTransactions.AddTransactions(T1);

		T2.coinID = "100002";
		T2.Source = obj.memberlist[2];
		T2.Destination = obj.memberlist[1];
		T2.coinsrc_block = tb1;
		obj.pendingTransactions.AddTransactions(T2);	

		T3.coinID = "100006";
		T3.Source = obj.memberlist[2];
		T3.Destination = obj.memberlist[1];
		T3.coinsrc_block = tb2;
		obj.pendingTransactions.AddTransactions(T3);	

		try{
			obj.memberlist[1].MineCoin(obj);
		}		
		catch(Exception e){
			System.out.println(e);
		}

		TransactionBlock tb3 = obj.bChain.lastBlocksList[0];


		if(tb3.dgst.equals("00009A7F99D2D09244E99D1F55AD29B49D335D9254A2EC682341ECFBC905AF4C")){
			if(tb3.nonce.equals("1000052544")){
				score += 3;
			}
		}


		if(tb3.trsummary.equals("036462EC76F2BEDA0CE4822E8747FEC9B69671359E7E3D2BD96485748360FD62")){
			score += 5;
		}

		if(obj.bChain.checkTransactionBlock(tb3) == true){
			score += 1;
		}

		tb3.nonce = "1000052543";
		if(obj.bChain.checkTransactionBlock(tb3) == false){
			score += 1;
		}


		System.out.println(score);


	}
}
