package rest.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import rest.pojo.User;
import rest.pojo.UserRequestObj;
import rest.pojo.UserResponseObj;
import rest.service.manager.UserManager;

@Path("/")
@Produces({ "application/json" })
@Consumes({ "application/json", "application/xml" })
public class UserService {
	@POST
	@Path("/getUser")
	public User getUser(UserRequestObj userRequest) {
		System.out.println("getUser is called....");
		User usr = null;
		try {
			usr = UserManager.getInstance().findUser(userRequest.getUser().getId(),userRequest.getUser().getName(),userRequest.getUser().getSsn());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usr;
	}

	@POST
	@Path("/findAllUsers")
	public UserResponseObj findAllUsers() {
		System.out.println("findAllUsers is called.....");
		UserResponseObj response = new UserResponseObj();
		List<User> userList = new ArrayList<User>();
		try {
			userList = UserManager.getInstance().listAllUsers();
			response.setUsers(userList);
			response.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("failure");
			response.setDescription(e.getMessage());
		}
		return response;
	}

	@POST
	@Path("/saveUser")
	public String saveUser(UserRequestObj userRequest) {
		System.out.println("saveUser is called.....");
		String statusMsg = "User Saved Successfully";
		try {
			UserManager.getInstance().creatUser(userRequest.getUser());
		} catch (Exception e) {
			e.printStackTrace();
			statusMsg = "Exception Occurred: "+e.getMessage();
		}
		return statusMsg;
	}

	@POST
	@Path("/deleteUser")
	public String deleteUser(@QueryParam("userId") String userId) {
		System.out.println("deleteUser is called....");
		String statusMsg = "User deleted successfully";
		try {
			UserManager.getInstance().deleteUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			statusMsg = "Exception Occurred: "+e.getMessage();
		}
		return statusMsg;
	}

	@POST
	@Path("/updateUser")
	public String updateUser(UserRequestObj userRequest) {
		System.out.println("updateUser is called.....");
		String statusMsg = "User updated successfully";
		try {
			UserManager.getInstance().updateUser(userRequest.getUser());
		} catch (Exception e) {
			e.printStackTrace();
			statusMsg = "Exception Occurred: "+e.getMessage();
		}
		return statusMsg;
	}
}
