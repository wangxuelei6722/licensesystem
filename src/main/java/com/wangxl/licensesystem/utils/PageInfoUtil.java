package com.wangxl.licensesystem.utils;

import java.io.Serializable;
/**
 * @ClassName PageInfoUtil
 * @Description : 功能说明 分页工具类
 *
 * @Author : Wangxl
 * @Date : 2020/5/8 11:13
*/
public class PageInfoUtil  implements Serializable
{
	/* 总条数 */
	private int totalRecord;
	/* 每页多少条 */
	private int pageSize = 20;

	/* 总页数 */
	private int totalPage;
	/* 当前页 */
	private int currentPage;
	/* 上一页 */
	private int prePage;
	/* 下一页 */
	private int nextPage;

	/*
	 * 当前页对应的条数 如果每页10条, 1 1 2 11 3 21
	 * 
	 * limit ?(currRecord),?(pageSize)
	 */
	private int currRecord;

	public int getTotalRecord()
	{
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord)
	{
		this.totalRecord = totalRecord;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getCurrentPage()
	{
		/*
		 * -1,
		 * 100000>总页数的时候
		 * 获取当前页的时候要做一个判断
		 * */
		if(this.currentPage < 1)
		{
			this.currentPage = 1 ; 
		}
		if(this.getTotalPage() > 0 && this.currentPage > this.getTotalPage())
		{
			this.currentPage = this.getTotalPage() ; 
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getTotalPage()
	{
		/*
		 * 总页数
		 * 总记录数:每页多少条,
		 * 总页数????咋算?
		 * 
		 * 总记录数,每页多少条
		 * 21		10		3
		 * 20		10 		2
		 * */
		if(this.totalRecord % this.pageSize == 0)
		{
			this.totalPage = this.totalRecord / this.pageSize; 
		}else
		{
			this.totalPage = this.totalRecord / this.pageSize + 1;
		}
		return totalPage;
	}

	public int getPrePage()
	{
		this.prePage = this.getCurrentPage() - 1 ;
		/*
		 * 总页数为:5
		 * this.getCurrentPage():的值:1 --- > 5
		 * 上页一页的值:0-->4
		 * 所以上一页的取值范围木有必须和总页数相比,因为上一页始终小于总页数
		 * */
		if(this.prePage < 1)
		{
			this.prePage = 1 ; 
		}
		return prePage;
	}

	public int getNextPage()
	{
		this.nextPage = this.getCurrentPage() + 1 ;
		/*
		 * 总页数为:5
		 * this.getCurrentPage():的值:1 --- > 5
		 * 下一页的值:2-->6
		 * 所以上一页的取值范围木有必须和总页数相比,因为上一页始终小于总页数
		 * */
		if(this.getTotalPage() > 0 && this.nextPage > this.getTotalPage())
		{
			this.nextPage = this.getTotalPage() ; 
		}
		return nextPage;
	}

	public int getCurrRecord()
	{
		/*
		 *当前页的条数:
		 * 每页10条
		 * 当前页	当前页的条数
		 * 1		1
		 * 2		11
		 * 3		21
		 * 4		31
		 * 5		51
		 * 数据库的索引值是从0开始的
		 * SELECT * FROM `a_admins` LIMIT 0, 1000
		 * 因为数据库的原因,不应该+1
		 */
		this.currRecord = (this.getCurrentPage() - 1 ) * this.pageSize; 
		return currRecord;
	}
	
	public static void main(String[] args)
	{
		PageInfoUtil pageInfoUtil = new PageInfoUtil() ; 
		pageInfoUtil.setTotalRecord(50);
		pageInfoUtil.setPageSize(10);
		pageInfoUtil.setCurrentPage(1000);
		System.out.println(pageInfoUtil);
	}
}
