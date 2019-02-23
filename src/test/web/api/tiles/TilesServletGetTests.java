package test.web.api.tiles;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.web.api.tile.APITile;
import com.web.api.tile.Tile;
import com.web.api.tile.TilesServlet;
import com.web.api.tile.TilesServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpServletTest;

public class TilesServletGetTests extends LabyrinthHttpServletTest
{
	private TilesServlet servlet;
	private TilesServletActions actions;
	private ArrayList<Tile> tiles = new ArrayList<>();
	private Tile tile;
	private User user;
	
	@Before
	public void setup() throws IOException
	{
		super.testSetup();
		
		actions = mock(TilesServletActions.class);
		tile = mock(Tile.class);
		user = mock(User.class);
		
		servlet = new TilesServlet();
		servlet.setTile(tile);
		servlet.setActions(actions);
	}

	@Test
	public void testTileIdLessThanZero() throws IOException, ServletException
	{
		String errorMessage = messages.getMessage("tile.need_id");
		int tileId = (-1) * rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileIdZero() throws IOException, ServletException
	{
		String errorMessage = messages.getMessage("tile.need_id");
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(0);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testAuthenticateUserReturnsNull() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = messages.getMessage("user.no_authorization");
		int tileId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenReturn(null);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testAuthenticateUserThrowsException() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = "Exception";
		int tileId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenThrow(new LabyrinthException("Exception"));

		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileLoadThrowsException() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = "Exception";
		int tileId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(0,  tileId,  0)).thenThrow(new LabyrinthException("Exception"));

		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileLoadReturnsEmptyArray() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = messages.getMessage("tile.no_tiles_found");
		int tileId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(0,  tileId,  0)).thenReturn(tiles);

		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileLoadReturnsOneTile() throws IOException, ServletException, LabyrinthException
	{
		int tileId = rand.nextInt(1000);
		Tile t = new Tile(0, 0, null);
		tiles.add(t);
		
		APITile apiTile = new APITile(t);
		String apiTileJson = gson.toJson(apiTile);
		JsonObject apiTileObj = gson.fromJson(apiTileJson, JsonObject.class);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(0,  tileId,  0)).thenReturn(tiles);

		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(apiTileObj.entrySet().size(), messageObj.entrySet().size());
	}

	@Test
	public void testTileLoadReturnsMultipleTiles() throws IOException, ServletException, LabyrinthException
	{
		int tileId = rand.nextInt(1000);
		Tile t = new Tile(0, 0, null);
		tiles.add(t);
		tiles.add(t);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES)).thenReturn(tileId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(0,  tileId,  0)).thenReturn(tiles);

		servlet.doGet(request, response);
		
		String response = strWriter.getBuffer().toString();
		JsonArray jsonArray = gson.fromJson(response, JsonArray.class);

		assertEquals(2, jsonArray.size());
	}
}
