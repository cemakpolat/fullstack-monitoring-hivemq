using System;
using System.Threading;
using System.Threading.Tasks;
using Serilog;
using System.Configuration;
using MQTTnet;
using MQTTnet.Client;
using MQTTnet.Client.Options;

class Program
{
    
    const int sleepDuration = 30000;
    static async Task Main(string[] args)
    {
            Log.Logger = new LoggerConfiguration()
                .MinimumLevel.Information()
                .Enrich.FromLogContext()
                .WriteTo.Console()
                .CreateLogger();
            

        String mqttServerURL = ConfigurationManager.AppSettings.Get("brokerUrl");
        String mqttServerPort = ConfigurationManager.AppSettings.Get("brokerPort");
        String mqttTopic = ConfigurationManager.AppSettings.Get("topic");
        // String client = ConfigurationManager.AppSettings.Get("mqttClientId");
        var factory = new MqttFactory();
        var mqttClient = factory.CreateMqttClient();
        
        var options = new MqttClientOptionsBuilder()
            .WithTcpServer(mqttServerURL,int.Parse(mqttServerPort))
            .WithClientId(Guid.NewGuid().ToString())
            .Build();

        mqttClient.UseConnectedHandler(async e =>
        {
            Log.Information("Connected to HiveMQ");
            await mqttClient.SubscribeAsync(new MqttTopicFilterBuilder().WithTopic(mqttTopic).Build());
        });

        mqttClient.UseDisconnectedHandler(async e =>
        {
            // Console.WriteLine("Disconnected from HiveMQ");
            Log.Information("Disconnected from HiveMQ");
            await Task.Delay(TimeSpan.FromSeconds(5));
            await mqttClient.ConnectAsync(options, CancellationToken.None);
        });

        mqttClient.UseApplicationMessageReceivedHandler(e =>
        {
            Log.Information($"Received message: {e.ApplicationMessage.ConvertPayloadToString()}");
            // Console.WriteLine($"Received message: {e.ApplicationMessage.ConvertPayloadToString()}");
        });

        
        // Connect to the MQTT broker
        await mqttClient.ConnectAsync(options);

        // Keep the application running until manually stopped
        while (true)
        {
            await Task.Delay(1000);
        }

        // await mqttClient.DisconnectAsync();
    }
}

