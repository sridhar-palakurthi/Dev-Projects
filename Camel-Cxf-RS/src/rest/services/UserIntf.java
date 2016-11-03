package rest.services;

import javax.jws.WebService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.pojo.UserRequestObj;

@Path("/")
@WebService
public interface UserIntf {

	@POST
	@Path("/saveUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveUser(UserRequestObj userRequest);

	@POST
	@Path("/updateUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(UserRequestObj userRequest);

	@POST
	@Path("/deleteUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@QueryParam("userId") String userId);

	@POST
	@Path("/getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(UserRequestObj userRequest);

	@POST
	@Path("/findAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllUsers();

}
