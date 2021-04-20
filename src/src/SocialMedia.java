package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//keep track of users following to every user
class UserNode
{	
	int uid;
	UserNode next;
}

//keep track of posts by every user
class UserPostNode
{	
	int pid;
	UserPostNode next;
}

//database for every pid
class PostNode
{
	int ts;
	int likes;
	int uid;
}

class DbPosts implements Comparable<DbPosts>
{
	int ts;
	int likes;
	int pid;
	
	SocialMedia obj=new SocialMedia();
	
	

	@Override
	public int compareTo(DbPosts  o) 
	{
		int t1=obj.timeStamp-this.ts;
		int t2=obj.timeStamp-o.ts;
		
		//if timestamp of t1 is less than 1000secs
		if(t1 <=1000 && t2 >1000)
		{
			return 1;
		}
		//if timestamp of t2 is less than 1000secs
		else if(t1 >1000 && t2 <=1000)
		{
			return 0;
		}
		//if timestamp of both t1,t2  > 1000secs, sort based on time stamp
		else if(t1 >1000 && t2 >1000)
		{
			if(this.ts> o.ts)
				return 1;
			else 
				return 0;
		}else  /*is ts of both t1,t2 <1000, sort based on likes and then time stamp*/
		{
			if(this.likes> o.likes)
				return 1;
			else if(this.likes < o.likes)
				return 0;
			else
			{
				if(this.ts > o.ts)
					return 1;
				else
					return 0;
				
			}
		}
	}
}


public class SocialMedia 
{
	private static int maxUsers=1001;
	private static int max=100001;
	
	//total postIds index, timestamp while comparing 
	int totalPids,timeStamp;
	
	
	//2d array for who follows who 
	boolean userFollow[][];
	
	//for UserNode
	UserNode userDb[];
	
	//for UserPostNode
	UserPostNode tsDb[];
	
	//for database
	PostNode postDb[];
	
	
	public SocialMedia()
	{
		userFollow=new boolean[maxUsers][maxUsers];
		userDb=new UserNode[maxUsers];
		tsDb=new UserPostNode[maxUsers];
		postDb=new PostNode[max];
		
	}
	void init(int N)
	{
		
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j <= N; j++) {
				userFollow[i][j]=false;
			}
		}
		
		for (int i = 0; i <= N; i++) {
			userDb[i]=null;
			tsDb[i]=null;
		}
		
		//for 2nd test case
		for (int i = 0; i <= totalPids; i++) {
			postDb[i].likes=0;
			postDb[i].ts=0;
			postDb[i].uid=0;
		}
		
		totalPids=0;
		
	}
	
	//make new UserNode
	UserNode getUserNode(int uid)
	{
		UserNode newNode=new UserNode();
		newNode.next=null;
		newNode.uid=uid;
		return newNode;
	}
	
	//make new UserPostNode
	UserPostNode getUserPostNode(int pid)
	{
		UserPostNode newNode=new UserPostNode();
		newNode.next=null;
		newNode.pid=pid;
		return newNode;
	}
	
	
	
	void follow(int uid1, int uid2, int timestamp)
	{
		//follow yourself
		if(userFollow[uid1][uid1]==false)
		{
			
			userFollow[uid1][uid1]=true;
			UserNode newNode=getUserNode(uid1);
			userDb[uid1]=newNode;
		}
		
		//follow user2
		userFollow[uid1][uid2]=true;
		UserNode newNode1=getUserNode(uid2);
		newNode1.next=userDb[uid1];
		userDb[uid1]=newNode1;
	
	}
	
	void makePost(int uid, int pid, int timestamp)
	{
		//global post index
		totalPids++;
		
		//if post is never made 
		if(postDb[pid].ts==0)
		{
			postDb[pid].likes=0;
			postDb[pid].ts=	timestamp;
			postDb[pid].uid=uid;
			
			//add to the tsDb 
			UserPostNode newnode=getUserPostNode(pid);
			newnode.next=tsDb[uid];
			tsDb[uid]=newnode;
		}
	}
	
	void like(int pid, int timestamp)
	{
		//if post is present 
		if(postDb[pid].ts!=0)
		{
			postDb[pid].likes++;
		}
	}
	
	
	void getFeed(int uid, int timestamp, int[] pidList)
	{
		//copy to global for comparing
		timeStamp=timestamp;
		
		//list keeping Nodes(pid,likes,ts) of uid following himself and other users
		ArrayList<DbPosts> list=null;
		
		//head of uid
		UserNode head=userDb[uid];
		
		
		while(head!=null)
		{
			//traverse all the posts made by the user(uid)
			UserPostNode currUserPost=tsDb[head.uid];
			
			list=new ArrayList<DbPosts>();
			DbPosts newnode=new DbPosts();
			
			while(currUserPost!=null)
			{
				newnode.pid=currUserPost.pid;
				newnode.likes=postDb[currUserPost.pid].likes;
				newnode.ts=postDb[currUserPost.pid].ts;
				
				
				list.add(newnode);
				currUserPost=currUserPost.next;
			}
			
			head=head.next;
		}
		
		//sort based on criteria
		Collections.sort(list);
		
		//take first 10 posts 
		if(list!=null)
		{ 
			for (int i = 0; i < 10; i++) {
				pidList[i]=list.get(i).pid;
			}
			
		}
		
		for (int i = 0; i < pidList.length; i++) {
			System.out.println(pidList[i]);
		}
		
	}
	
	
	public static void main(String[] args)
	{
		SocialMedia p=new SocialMedia();
		p.follow(1, 2, 1);
		p.follow(2, 1, 1);
		p.getFeed(2, 534, new int [max]);
		p.getFeed(2, 766, new int [max]);
		p.getFeed(1, 1088, new int [max]);
		p.makePost(1, 1, 1752);
		p.like(1, 1861);
		p.makePost(1, 2, 2027);
		p.makePost(2, 3, 2117);
		p.makePost(1, 4, 2163);
		p.getFeed(2, 2476, new int [max]);
		p.like(2, 2494);
		p.like(1, 2542);
		p.makePost(1, 5, 2666);
		p.getFeed(2, 2853, new int [max]);
		p.like(3, 2944);
		p.getFeed(2, 3033, new int [max]);
		
		


	}

}
