package howbuy.android.piggy.api.dto;

import java.util.List;

/**
 * 绑卡上传参数
 * @author Administrator
 *
 */
public class BindCardUploadParams {
	private List<Channels> channels;
	
	
	public List<Channels> getChannels() {
		return channels;
	}

	public void setChannels(List<Channels> channels) {
		this.channels = channels;
	}
	
	
	public static class Channels{
		private String name;
		private String version;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public Channels(String name, String version) {
			super();
			this.name = name;
			this.version = version;
		}
		
		
	}

	
	
}
