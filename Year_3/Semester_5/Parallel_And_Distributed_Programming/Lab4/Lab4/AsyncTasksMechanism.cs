using System.Net;
using System.Net.Sockets;
using System.Text;
using Lab4.Utils;

namespace Lab4;

public class AsyncTasksMechanism
{
    private static List<string> _hosts;
    private static List<Task> _tasks;
    private static ConsoleLogger _logger = new ConsoleLogger(false);

    public static void Run(List<string> hostnames)
    {
        _hosts = hostnames;
        _tasks = new List<Task>();
        for (var i = 0; i < _hosts.Count; i++)
        {
            _tasks.Add(Task.Factory.StartNew(DoStart, i));
        }
        Task.WaitAll(_tasks.ToArray());
    }

    private static void DoStart(object idObject)
    {
        var id = (int)idObject;
        StartClient(_hosts[id], id);
    }

    private static async void StartClient(string host, int id)
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
        
        await Connect(state);
        
        await Send(state, Http.GetRequestString(state.hostname, state.endpointPath));
        
        await Receive(state);
        
        _logger.Show(state.responseContent.ToString());
        
        client.Shutdown(SocketShutdown.Both);
        client.Close();
    }

    private static async Task Connect(CustomState state)
    {
        state.socket.BeginConnect(state.remoteEndPoint, ConnectCallback, state);
        await Task.FromResult<object>(state.connectDone.WaitOne());
    }
    
    private static void ConnectCallback(IAsyncResult ar)
    {
        var state = (CustomState)ar.AsyncState;
        var clientSocket = state.socket;
        var clientID = state.clientID;
        var hostname = state.hostname;
        
        clientSocket.EndConnect(ar);
        
        _logger.Show($"{clientID}) Socket connected to {hostname} ({clientSocket.RemoteEndPoint})");
        
        state.connectDone.Set();
    }
    
    private static async Task Send(CustomState state, string data)
    {
        var byteData = Encoding.ASCII.GetBytes(data);
        state.socket.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);
        
        await Task.FromResult<object>(state.sendDone.WaitOne());
    }
    
    private static void SendCallback(IAsyncResult ar)
    {
        var state = (CustomState)ar.AsyncState;
        var clientSocket = state.socket;
        var clientID = state.clientID;
        
        var bytesSent = clientSocket.EndSend(ar);
        _logger.Show($"{clientID}) Sent {bytesSent} bytes to the server.");
        
        state.sendDone.Set();
    }
    
    private static async Task Receive(CustomState state)
    {
        state.socket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, ReceiveCallback, state);
        await Task.FromResult<object>(state.receiveDone.WaitOne());
    }
    
    private static void ReceiveCallback(IAsyncResult ar)
    {
        var state = (CustomState)ar.AsyncState;
        var clientSocket = state.socket;

        try
        {
            var bytesRead = clientSocket.EndReceive(ar);
            
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
        } catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
}