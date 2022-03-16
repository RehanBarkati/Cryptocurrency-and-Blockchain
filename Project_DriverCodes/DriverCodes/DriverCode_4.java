package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;

public class DriverCode_4{
	public static void main(String args[]){
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

		int score = 0;
		
		DSCoin_Honest obj = new DSCoin_Honest();	
        obj.memberlist = new Members[4];
        obj.memberlist[0] = m1;
        obj.memberlist[1] = m2;
        obj.memberlist[2] = m3;
        obj.memberlist[3] = m4;
		obj.pendingTransactions = new TransactionQueue();
		obj.bChain = new BlockChain_Honest();
		obj.bChain.tr_count = 4;

		Moderator mod = new Moderator();
		try{
			mod.initializeDSCoin(obj,8);
		}
		catch(Exception e){
			System.out.println(e);
		}

		TransactionBlock tb1 = obj.bChain.lastBlock.previous;
		TransactionBlock tb2 = obj.bChain.lastBlock;

		try{
			m1.initiateCoinsend("102",obj);
			m3.initiateCoinsend("102",obj);
			m3.initiateCoinsend("102",obj);		
		}
		catch(Exception e){
			System.out.println(e);
		}


		try{
			m2.MineCoin(obj);
		}
		catch(Exception e){
			System.out.println(e);
		}


		//checking nonce
		if(obj.bChain.lastBlock.nonce.equals("1000052544")){
			score += 5;
		}

		//checking digest
		if(obj.bChain.lastBlock.dgst.equals("00009A7F99D2D09244E99D1F55AD29B49D335D9254A2EC682341ECFBC905AF4C")){
			score += 2;
		}

		//checking previous 
		if(obj.bChain.lastBlock.previous.equals(tb2)){
			score += 3;
		}

		try{
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p1 = m1.finalizeCoinsend(obj.bChain.lastBlock.trarray[0],obj);
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p2 = m3.finalizeCoinsend(obj.bChain.lastBlock.trarray[1],obj);
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p3 = m3.finalizeCoinsend(obj.bChain.lastBlock.trarray[2],obj);
			//System.out.println(obj.bChain.lastBlock.trarray[0].coinID +" "+ obj.bChain.lastBlock.trarray[0].Source.UID +" "+ obj.bChain.lastBlock.trarray[0].Destination.UID);
			//System.out.println(obj.bChain.lastBlock.trarray[1].coinID +" "+ obj.bChain.lastBlock.trarray[1].Source.UID +" "+ obj.bChain.lastBlock.trarray[1].Destination.UID);
			//System.out.println(obj.bChain.lastBlock.trarray[2].coinID +" "+ obj.bChain.lastBlock.trarray[2].Source.UID +" "+ obj.bChain.lastBlock.trarray[2].Destination.UID);
		}
		catch(Exception e){
			System.out.println(e);
		}	

		TransactionBlock tb3 = obj.bChain.lastBlock;

		Transaction T1 = new Transaction();
		Transaction T2 = new Transaction();
		Transaction T3 = new Transaction();
		Transaction T4 = new Transaction();

		T1.coinID = "100000";
		T2.coinID = "100000";
		T3.coinID = "100001";
		T4.coinID = "100003";
		
		T1.Source = m2;
		T1.Destination = m1; 
		T1.coinsrc_block = tb3;

		T2.Source = m1;
		T2.Destination = m3; 
		T2.coinsrc_block = tb1;

		T3.Source = m2;
		T3.Destination = m1; 
		T3.coinsrc_block = tb1;

		T4.Source = m4;
		T4.Destination = m1; 
		T4.coinsrc_block = tb1;

		obj.pendingTransactions.AddTransactions(T1);
		obj.pendingTransactions.AddTransactions(T2);
		obj.pendingTransactions.AddTransactions(T3);
		obj.pendingTransactions.AddTransactions(T4);
		
		/*try{
			m2.initiateCoinsend("101",obj);
			m3.initiateCoinsend("101",obj);
			m2.initiateCoinsend("101",obj);
			m4.initiateCoinsend("101",obj);
		}
		catch(Exception e){
			System.out.println(e);
		}*/

		try{
			m3.MineCoin(obj);
		}
		catch(Exception e){
			System.out.println(e);
		}

		//checking nonce
		if(obj.bChain.lastBlock.nonce.equals("1000188337")){
			score += 5;
		}

		//checking digest
		if(obj.bChain.lastBlock.dgst.equals("00004F4D8749BFFE9E1BEF4152F6849BCD0A529B463FF0D409B338AFABF690C8")){
			score += 2;
		}

		//checking previous
		if(obj.bChain.lastBlock.previous.equals(tb3)){
			score += 3;
		}

	/*
		try{
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p4 = obj.memberlist[1].finalizeCoinsend(obj.bChain.lastBlock.trarray[0],obj);
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p5 = obj.memberlist[1].finalizeCoinsend(obj.bChain.lastBlock.trarray[1],obj);
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> p6 = obj.memberlist[3].finalizeCoinsend(obj.bChain.lastBlock.trarray[2],obj);
			//System.out.println(obj.bChain.lastBlock.trarray[0].coinID +" "+ obj.bChain.lastBlock.trarray[0].Source.UID +" "+ obj.bChain.lastBlock.trarray[0].Destination.UID);
			//System.out.println(obj.bChain.lastBlock.trarray[1].coinID +" "+ obj.bChain.lastBlock.trarray[1].Source.UID +" "+ obj.bChain.lastBlock.trarray[1].Destination.UID);
			//System.out.println(obj.bChain.lastBlock.trarray[2].coinID +" "+ obj.bChain.lastBlock.trarray[2].Source.UID +" "+ obj.bChain.lastBlock.trarray[2].Destination.UID);
		}
		catch(Exception e){
			System.out.println(e);
		}	*/

		System.out.println((float)score/2);

	}
}

