package ca.discoverygarden.gsearch_extensions;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class URIResolver implements javax.xml.transform.URIResolver {
	protected CloseableHttpClient client;
	public static final String USER_AGENT_PROPERTY = "dgi_gsearch_extensions.uriresolver.useragent";

	public URIResolver() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		if (System.getProperties().containsKey(USER_AGENT_PROPERTY)) {
			builder.setUserAgent(System.getProperty(USER_AGENT_PROPERTY));
		}
		client = builder.build();
	}

	@Override
	protected void finalize() throws Throwable {
		client.close();
		super.finalize();
	}
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		HttpGet get = new HttpGet(href);
		try {
			Source source = new StreamSource(client.execute(get).getEntity().getContent());
			return source;
		} catch (Exception e) {
			throw new TransformerException("Error resolving '" + href + "'", e);
		}
	}

}
