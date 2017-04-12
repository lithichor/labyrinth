package test.tests.tiles;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.web.api.tile.APITile;
import com.web.api.tile.Tile;
import com.web.api.tile.TilesMapServlet;
import com.web.api.tile.TilesServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpTest;

public class TilesMapServletGetTests extends LabyrinthHttpTest
{
	private TilesMapServlet servlet;
	private TilesServletActions actions;
	private ArrayList<Tile> tiles = new ArrayList<>();
	private Tile tile;
	private User user;
	
	@Before
	public void setup() throws IOException, LabyrinthException
	{
		super.testSetup();
		
		actions = mock(TilesServletActions.class);
		tile = mock(Tile.class);
		user = mock(User.class);
		
		servlet = new TilesMapServlet();
		servlet.setActions(actions);
		servlet.setTile(tile);
		
		when(actions.authenticateUser(request)).thenReturn(user);
	}

	@Test
	public void testMapIdZero() throws IOException, ServletException
	{
		String errorMessage = messages.getMessage("tile.no_map_id");
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(0);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testMapIdLessThanZero() throws IOException, ServletException
	{
		String errorMessage = messages.getMessage("tile.no_map_id");
		int id = (-1) * rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(id);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testAuthenticateUserThrowsException() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = "Exception";
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(rand.nextInt(1000));
		when(actions.authenticateUser(request)).thenThrow(new LabyrinthException("Exception"));
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testAuthenticateUserReturnsNull() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = messages.getMessage("user.no_authorization");
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(rand.nextInt(1000));
		when(actions.authenticateUser(request)).thenReturn(null);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileLoadReturnsEmptyArray() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = messages.getMessage("tile.no_tiles_for_map");
		int mapId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(mapId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(mapId, 0, 0)).thenReturn(new ArrayList<Tile>());
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTileLoadThrowsException() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = "Exception";
		int mapId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(mapId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(mapId, 0, 0)).thenThrow(new LabyrinthException("Exception"));
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testLoadReturnsOneTile() throws IOException, ServletException, LabyrinthException
	{
		Tile newTile = new Tile(0, 0, null);
		tiles.add(newTile);
		APITile apiTile = new APITile(newTile);
		int mapId = rand.nextInt(1000);

		String apiTileJson = gson.toJson(apiTile);
		JsonObject apiTileObj = gson.fromJson(apiTileJson, JsonObject.class);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(mapId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(mapId, 0, 0)).thenReturn(tiles);
		
		servlet.doGet(request, response);
		
		String json = strWriter.getBuffer().toString();
		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);

		assertEquals(apiTileObj.entrySet().size(), jsonObj.entrySet().size());
	}

	@Test
	public void testLoadReturnsMultipleTiles() throws IOException, ServletException, LabyrinthException
	{
		tiles.add(new Tile(0, 0, null));
		tiles.add(new Tile(0, 0, null));
		int mapId = rand.nextInt(1000);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS)).thenReturn(mapId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(tile.load(mapId, 0, 0)).thenReturn(tiles);
		
		servlet.doGet(request, response);
		
		String json = strWriter.getBuffer().toString();
		JsonArray jsonArray = gson.fromJson(json, JsonArray.class);

		assertEquals(2, jsonArray.size());
	}
}
