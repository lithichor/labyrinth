package com.web.api.tile;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Tile;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APITile;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class TileServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -1333490555879202438L;
	
	/**
	 * GET a specific Tile, identified by the tileId
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		User user;
		int tileId = 0;
		TileServletActions actions = new TileServletActions();
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<APITile> apiTiles = new ArrayList<>();
		
		String urlStr = splitUrl(request.getRequestURI(), EndpointsWithIds.TILES);
		
		// this means an ID was included in the URL
		if(urlStr.length() > 0)
		{
			tileId = parseIdFromString(urlStr);
		}
		
		// if no id is provided, return an error - we do not
		// return all tiles for the user, and there's no way to
		// know or guess which map is being referenced
		if(tileId == 0)
		{
			errors.add(messages.getMessage("tile.need_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		try
		{
			user = actions.authenticateUser(request, response);
			
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
			// provide userId to prevent cross tenant bug
			tiles = new Tile(0, 0, null).load(0, tileId, user.getId());
			for(Tile t: tiles)
			{
				apiTiles.add(new APITile(t));
			}
			
			if(tiles.size() == 0)
			{
				errors.add(messages.getMessage("tile.no_tiles_found"));
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
}
