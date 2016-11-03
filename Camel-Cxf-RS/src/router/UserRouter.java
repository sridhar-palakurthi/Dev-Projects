package router;

import java.util.List;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.pojo.User;
import rest.pojo.UserRequestObj;
import rest.service.manager.UserManager;
import rest.services.UserIntf;
import util.EntityToJsonBuilder;

public class UserRouter implements UserIntf {

	@Override
	public Response saveUser(UserRequestObj userRequest) {
		UserManager userMgr = UserManager.getInstance();
		String response = "User created successfully";
		try {
			userMgr.creatUser(userRequest.getUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response updateUser(UserRequestObj userRequest) {
		UserManager userMgr = UserManager.getInstance();
		String response = "User updated successfully";
		try {
			userMgr.updateUser(userRequest.getUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response deleteUser(String userId) {
		UserManager userMgr = UserManager.getInstance();
		String response = "User deleted successfully";
		try {
			userMgr.deleteUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response getUser(UserRequestObj userRequest) {
		UserManager userMgr = UserManager.getInstance();
		User user = null;
		try {
			user = userMgr.findUser(userRequest.getUser().getId(),userRequest.getUser().getName(),userRequest.getUser().getSsn());
			System.out.println("User Object is built: Id: "+user.getId()+" Name: "+user.getName()+" SSN: "+user.getSsn());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(user).build();
		//return Response.ok(user, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response findAllUsers() {
		// TODO Auto-generated method stub
		UserManager userMgr = UserManager.getInstance();
		List<User> userList = null;
		try {
			userList = userMgr.listAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(EntityToJsonBuilder.convertToJson(userList), MediaType.APPLICATION_JSON).build();
	}

}
