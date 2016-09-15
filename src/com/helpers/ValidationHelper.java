package com.helpers;

import java.util.ArrayList;
import java.util.HashMap;

public interface ValidationHelper
{
	public boolean validate(HashMap<String, String> params, ArrayList<String> errors);
}
