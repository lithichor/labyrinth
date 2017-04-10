package com.web.api.tile;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class TilesMapServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 6416254265624381493L;
	private ArrayList<Tile> tiles;
	private Tile tile;
	private TilesServletActions actions;
	
	public void setTiles(ArrayList<Tile> tiles) { this.tiles = tiles; }
	public void setTile(Tile tile) { this.tile = tile; }
	public void setActions(TilesServletActions actions) { this.actions = actions; }
	
	/**
	 * GET all the tiles for a specific Map
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		User user;
		int mapId = 0;
		actions = (actions == null) ? new TilesServletActions() : actions;
		tiles = (tiles == null) ? new ArrayList<>() : tiles;
		tile = (tile == null) ? new Tile(0, 0, null) : tile;
		ArrayList<APITile> apiTiles = new ArrayList<>();
		
		mapId = actions.getIdFromUrl(request, EndpointsWithIds.TILES_MAPS);
		
		// return an error if there is no map ID
		if(mapId <= 0)
		{
			errors.add(messages.getMessage("tile.no_map_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}

		try
		{
			user = actions.authenticateUser(request);
			
			// if authentication fails, return immediately
			if(user == null || errors.size() > 0)
			{
				if(user == null)
				{
					errors.add(messages.getMessage("user.no_authorization"));
				}
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}

			// load tiles and add to array list of API tiles
			tiles = tile.load(mapId, 0, user.getId());
			for(Tile t: tiles)
			{
				apiTiles.add(new APITile(t));
			}

			if(tiles.size() == 0)
			{
				errors.add(messages.getMessage("tile.no_tiles_for_map"));
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}
			else if(tiles.size() == 1)
			{
				apiOut(gson.toJson(apiTiles.get(0)), response);
			}
			else
			{
				apiOut(gson.toJson(apiTiles), response);
			}
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		TilesMapOptions options = new TilesMapOptions();
		apiOut(gson.toJson(options), response);
	}
}
