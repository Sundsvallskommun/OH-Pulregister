package se.unlogic.hierarchy.foregroundmodules.test.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.foregroundmodules.rest.ResponseHandler;
import se.unlogic.standardutils.json.JsonNode;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.webutils.http.HTTPUtils;


public class JsonResponseHandler implements ResponseHandler<JsonNode> {

	@Override
	public Class<? extends JsonNode> getType() {

		return JsonNode.class;
	}

	@Override
	public void handleResponse(JsonNode jsonNode, HttpServletResponse res) throws IOException {

		HTTPUtils.sendReponse(jsonNode.toJson(), JsonUtils.getContentType(), res);
	}

}
