using System.Net;
using System.Net.Sockets;
using System.Text;
using Lab4.Utils;

namespace Lab4;

public class DirectCallbacks
{
    private static List<string> _hosts;
    private static ConsoleLogger _logger = new ConsoleLogger(false);

    public static void Run(List<string> hostnames)
    {
        _hosts = hostnames;
        for (int i = 0; i < _hosts.Count; i++)
        {
            DoStart(i);
        }
    }

    private static void DoStart(int id)
    {
        StartClient(_hosts[id], id);
    }

    private static void StartClient(string host, int id)
    {
        var hostInfo = Dns.GetHostEntry(host.Split("/")[0]);
        var ipAddress = hostInfo.AddressList[0];
        var remoteEndpoint = new IPEndPoint(ipAddress, Http.PORT);
        
        var clientSocket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
        
        var state = new CustomState
        {
            socket = clientSocket,
            hostname = host.Split("/")[0],
            endpointPath = host.Contains("/") ? host.Substring(host.IndexOf("/")) : "/",
            remoteEndPoint = remoteEndpoint,
            clientID = id
        };
        
        state.socket.BeginConnect(state.remoteEndPoint, OnConnect, state);
    }

    private static void OnConnect(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientSocket = state.socket;
        var clientID = state.clientID;
        var hostname = state.hostname;
        
        clientSocket.EndConnect(result);
        _logger.Show($"{clientID}) Socket connected to {hostname} ({clientSocket.RemoteEndPoint})");
        
        var byteData = Encoding.ASCII.GetBytes(Http.GetRequestString(state.hostname, state.endpointPath));
        
        state.socket.BeginSend(byteData, 0, byteData.Length, 0, OnSend, state);
    }
    
    private static void OnSend(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientID = state.clientID;
        
        var bytesSent = state.socket.EndSend(result);
        _logger.Show($"{clientID}) Sent {bytesSent} bytes to server.");
        
        state.socket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, OnReceive, state);
    }

    private static void OnReceive(IAsyncResult result)
    {
        var state = (CustomState)result.AsyncState;
        var clientID = state.clientID;

        try
        {
            var bytesRead = state.socket.EndReceive(result);
            state.responseContent.Append(Encoding.ASCII.GetString(state.receiveBuffer, 0, bytesRead));
            
            if (!Http.ResponseHeaderFullyObtained(state.responseContent.ToString()))
            {
                state.socket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, OnReceive, state);
            }
            else
            {
                var responseBody = Http.GetResponseBody(state.responseContent.ToString());
                var contentLength = Http.GetContentLength(state.responseContent.ToString());
                if (responseBody.Length < contentLength)
                {
                    state.socket.BeginReceive(state.receiveBuffer, 0, CustomState.BUFFER_SIZE, 0, OnReceive, state);
                }
                else
                {
                    _logger.Show(state.responseContent.ToString());
                    
                    state.socket.Shutdown(SocketShutdown.Both);
                    state.socket.Close();
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
}