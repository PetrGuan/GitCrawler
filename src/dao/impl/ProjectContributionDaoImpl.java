package dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.ProjectContribution;
import dao.DaoHelper;
import dao.ProjectContributionDao;
import factory.DaoFactory;

public class ProjectContributionDaoImpl implements ProjectContributionDao{

	
	private static ProjectContributionDaoImpl projectContributionDaoImpl=new ProjectContributionDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	public static ProjectContributionDaoImpl getInstance(){
		return projectContributionDaoImpl;
	}
	
	
	@Override
	public boolean addProjectContribution(
			ProjectContribution projectContribution) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("INSERT INTO `gitcrawler`.`project_contribution` (`project_id`,`developer_id`,`contributions`) VALUES (?,?,?)");
			ps.setInt(1,projectContribution.getProject_id());
			ps.setInt(2, projectContribution.getDeveloper_id());
			ps.setInt(3, projectContribution.getContributions());
			ps.execute();			
			return true;
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return false;
	}


	@Override
	public ProjectContribution findProjectContribution(int project_id,
			int developer_id) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
			ps=con.prepareStatement("select * from project_contribution where project_id =? and developer_id=?");
			ps.setInt(1, project_id);
			ps.setInt(2, developer_id);
			rs=ps.executeQuery();
			ProjectContribution pcb = null;
			if(rs.next()){
				pcb = new ProjectContribution();
				pcb.setProject_id(project_id);
				pcb.setDeveloper_id(developer_id);
				pcb.setContributions(rs.getInt("contributions"));
			}	
			
			return pcb;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return null;
	}


	@Override
	public boolean updateProjectContribution(ProjectContribution pct) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		try{
			ps=con.prepareStatement("UPDATE `gitcrawler`.`project_contribution` SET `project_id`=?, `developer_id`=?,`contributions`=? where `project_id`=? and `developer_id`=?");
			ps.setInt(1,pct.getProject_id());
			ps.setInt(2,pct.getDeveloper_id());
			ps.setInt(3,pct.getContributions());
			ps.setInt(4,pct.getProject_id());
			ps.setInt(5,pct.getDeveloper_id());
			ps.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		return false;
	}


	@Override
	public boolean deleteProjectContribution(ProjectContribution pct) {
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		
		try{
			ps=con.prepareStatement("delete from project_contribution where project_id=? and developer_id=?");
			ps.setInt(1, pct.getProject_id());
			ps.setInt(2, pct.getDeveloper_id());
			ps.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		return false;
	}


	@Override
	public List<ProjectContribution> findProjectContribution(
			String developerName) {
		
		List<ProjectContribution> results = new ArrayList<ProjectContribution>();
		int developer_id = DaoFactory.getDeveloperDao().findDeveloper(developerName).getId();
		Developer dp = new Developer();
		dp.setId(developer_id);
		List<usefuldata.Project> projects = DaoFactory.getDeveloperDao().findDeveloperForProjects(dp);
		
		Connection con=daoHelper.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try{
		
		for(int i = 0;i<projects.size();i++){
		
				ps=con.prepareStatement("select * from project_contribution where project_id =? and developer_id=?");
				ps.setInt(1, projects.get(i).getId());
				ps.setInt(2, developer_id);
				rs=ps.executeQuery();
				
				while(rs.next()){
					ProjectContribution pcb = new ProjectContribution();
					pcb = new ProjectContribution();
					pcb.setProject_id(projects.get(i).getId());
					pcb.setDeveloper_id(developer_id);
					pcb.setContributions(rs.getInt("contributions"));
					pcb.setProjectName(projects.get(i).getName());
					results.add(pcb);
				}	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			daoHelper.closeResult(rs);
			daoHelper.closePreparedStatement(ps);
			daoHelper.closeConnection(con);
		}
		
		
		return results;
	}

}