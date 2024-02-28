using System.Net;
using System.Net.Sockets;
using System.Text;
using Lab4.Utils;

namespace Lab4;

public class TasksMechanism
{
    private static List<string> _hosts;
    private static List<Task> _tasks;
    private static ConsoleLogger _logger = new ConsoleLogger(false);

    public static void Run(List<string> hostnames)
    {
        _hosts = hostnames;
        _tasks = new List<Task>();

        for (int i = 0; i < _hosts.Count; i++)
        {
            _tasks.Add(Task.Factory.StartNew(DoStart, i));
        }
        
        Task.WaitAll(_tasks.ToArray());
    }

    private static void DoStart(object idObject)
    {
        int id = (int)idObject;
        StartClient(_hosts[id], id);
    }

    private static void StartClient(string host, int id)
    {
        var ipHostInfo = Dns.GetHostEntry(host.Split('/')[0]);
        var ipAddress = ipHostInfo.AddressList[0];
        var remoteEndpoint = new IPEndPoint(ipAddress, Http.PORT);
        
        var client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
        
        var state = new CustomState
        {
            socket = client,
            hostname = host.Split('/')[0],
            endpointPath = host.Contains("/") ? host.Substring(host.IndexOf("/")) : "/",
            remoteEndPoint = remoteEndpoint,
            clientID = id
        };
        
        Connect(state).Wait();
        
        Send(state, Http.GetRequestString(state.hostname, state.endpointPath)).Wait();
        
        Receive(state).Wait();
        
        _logger.Show(state.responseContent.ToString());
        
        client.Shutdown(SocketShutdown.Both);
        client.Close();
    }

    private static Task Connect(CustomState state)
    {
        state.socket.BeginConnect(state.remoteEndPoint, ConnectCallback, state);
        return Task.FromResult(state.connectDone.WaitOne());
    }
    
    private static void ConnectCallback(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientSocket = state.socket;
        var clientId = state.clientID;
        var hostname = state.hostname;

        clientSocket.EndConnect(result);

        _logger.Show($"{clientId}) Socket connected to {hostname} ({clientSocket.RemoteEndPoint})");

        state.connectDone.Set();
    }

    private static Task Send(CustomState state, string data)
    {
        var byteData = Encoding.ASCII.GetBytes(data);
        state.socket.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);

        return Task.FromResult(state.sendDone.WaitOne());
    }
    
    private static void SendCallback(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientSocket = state.socket;
        var clientId = state.clientID;

        var bytesSent = clientSocket.EndSend(result);
        _logger.Show($"{clientId}) Sent {bytesSent} bytes to server.");

        state.sendDone.Set();
    }
    
    private static Task Receive(CustomState state)
    {
        state.socket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, ReceiveCallback, state);

        return Task.FromResult(state.receiveDone.WaitOne());
    }
    
    private static void ReceiveCallback(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientSocket = state.socket;

        try
        {
            var bytesRead = clientSocket.EndReceive(result);
            state.responseContent.Append(Encoding.ASCII.GetString(state.receiveBuffer, 0, bytesRead));

            if (!Http.ResponseHeaderFullyObtained(state.responseContent.ToString()))
            {
                clientSocket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, ReceiveCallback, state);
            }
            else
            {
                var responseBody = Http.GetResponseBody(state.responseContent.ToString());

                if (responseBody.Length < Http.GetContentLength(state.responseContent.ToString()))
                {
                    clientSocket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, ReceiveCallback, state);
                }
                else
                {
                    state.receiveDone.Set();
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
}