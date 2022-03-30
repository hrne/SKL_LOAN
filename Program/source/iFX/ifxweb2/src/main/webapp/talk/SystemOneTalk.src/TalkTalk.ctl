VERSION 5.00
Object = "{248DD890-BB45-11CF-9ABC-0080C7E7B78D}#1.0#0"; "MSWINSCK.OCX"
Begin VB.UserControl TalkTalk 
   ClientHeight    =   3330
   ClientLeft      =   0
   ClientTop       =   0
   ClientWidth     =   5355
   ScaleHeight     =   3330
   ScaleWidth      =   5355
   Begin MSWinsockLib.Winsock tcpClient 
      Left            =   5160
      Top             =   5160
      _ExtentX        =   741
      _ExtentY        =   741
      _Version        =   393216
   End
   Begin MSWinsockLib.Winsock Winsocks 
      Index           =   0
      Left            =   1800
      Top             =   5520
      _ExtentX        =   741
      _ExtentY        =   741
      _Version        =   393216
   End
   Begin MSWinsockLib.Winsock WinsockBroadCast 
      Left            =   4560
      Top             =   5520
      _ExtentX        =   741
      _ExtentY        =   741
      _Version        =   393216
      Protocol        =   1
      RemoteHost      =   "255.255.255.255"
   End
   Begin VB.CommandButton Command2 
      Caption         =   "bind udp"
      Height          =   255
      Left            =   3720
      TabIndex        =   6
      Top             =   2400
      Width           =   735
   End
   Begin VB.TextBox txtLocalPort 
      Height          =   375
      Left            =   3120
      TabIndex        =   5
      Text            =   "udp local port"
      Top             =   2760
      Width           =   1215
   End
   Begin VB.TextBox txtPort 
      Height          =   375
      Left            =   1560
      TabIndex        =   4
      Text            =   "remote udp port"
      Top             =   2760
      Width           =   1335
   End
   Begin VB.TextBox txtIp 
      Height          =   375
      Left            =   0
      TabIndex        =   3
      Text            =   "remote udp ip"
      Top             =   2760
      Width           =   1335
   End
   Begin VB.ListBox List1 
      Height          =   2010
      Left            =   0
      TabIndex        =   2
      Top             =   0
      Width           =   5055
   End
   Begin VB.CommandButton Command1 
      Caption         =   "send"
      Height          =   375
      Left            =   2760
      TabIndex        =   1
      Top             =   2280
      Width           =   855
   End
   Begin VB.TextBox Text1 
      Height          =   375
      Left            =   0
      TabIndex        =   0
      Text            =   "msg send via udp"
      Top             =   2280
      Width           =   2655
   End
   Begin MSWinsockLib.Winsock Winsock1 
      Left            =   240
      Top             =   5520
      _ExtentX        =   741
      _ExtentY        =   741
      _Version        =   393216
      Protocol        =   1
   End
End
Attribute VB_Name = "TalkTalk"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Option Explicit


Dim msgCol As New Collection

Dim udpLocalPort As Integer

Public Event DataArrivalUDP()
Public Event DataArrivalBroadCast()

Public Event DataArrivalClient(ByVal serverIp As String, ByVal strData As String)

' Events.
' ------------------------------------------------------------------------------
'
Public Event NewConnection(ByVal lngIndex As Long, ByVal clientIp As String, ByRef blnCancel As Boolean)
Public Event ConnectionClosed(ByVal lngIndex As Long, ByVal clientIp As String)
Public Event DataArrival(ByVal lngIndex As Long, ByVal clientIp As String, ByVal strData As String)
Public Event WinsockError(ByVal lngIndex As Long, ByVal lngNumber As Long, ByVal strSource As String, ByVal strDescription As String)
Public Event SendProgress(ByVal lngIndex As Long, ByVal lngBytesSent As Long, ByVal lngBytesRemaining As Long)
Public Event SendComplete(ByVal lngIndex As Long)
'
' ------------------------------------------------------------------------------
' Private variables.
' ------------------------------------------------------------------------------
'
Private m_blnRaiseErrors   As Boolean
Private m_strLastError     As String
Private m_lngLocalPort     As Long
Private m_lngMaxClients    As Long
Private m_lngActiveClients As Long
Private m_lngBytesReceived As Long
Private m_lngBytesSent     As Long

Private Declare Sub Sleep Lib "kernel32" (ByVal dwMilliseconds As Long)




'
' ------------------------------------------------------------------------------
' Properties.
' ------------------------------------------------------------------------------
'
Public Property Get RaiseErrors() As Boolean
   RaiseErrors = m_blnRaiseErrors
End Property
'
Public Property Let RaiseErrors(ByVal Value As Boolean)
   m_blnRaiseErrors = Value
End Property
'
Public Property Get LastError() As String
   LastError = m_strLastError
End Property
'
Public Property Let LastError(ByVal Value As String)
   m_strLastError = Value
End Property
'
Public Property Get MaxClients() As Long
   MaxClients = m_lngMaxClients
End Property
'
Public Property Let MaxClients(ByVal Value As Long)
   m_lngMaxClients = Value
End Property
'
Public Property Get ActiveClients() As Long ' Read only.
   ActiveClients = m_lngActiveClients
End Property
'
Public Property Get LocalPort() As Long
   LocalPort = m_lngLocalPort
End Property
'
Public Property Let LocalPort(ByVal Value As Long)
   m_lngLocalPort = Value
End Property
'
Public Property Get BytesReceived() As Long
   BytesReceived = m_lngBytesReceived
End Property
'
Public Property Let BytesReceived(ByVal Value As Long)
   m_lngBytesReceived = Value
End Property
'
Public Property Get BytesSent() As Long
   BytesSent = m_lngBytesSent
End Property
'
Public Property Let BytesSent(ByVal Value As Long)
   m_lngBytesSent = Value
End Property
'
Public Property Get SocketCount() As Long ' Read only
   SocketCount = Winsocks.Count - 1
End Property
'
' ------------------------------------------------------------------------------
' Client properties.
' ------------------------------------------------------------------------------
'
Public Property Get remoteHost(ByVal lngIndex As Long) As String  ' Read only.
   If (lngIndex > 0) And (lngIndex <= Winsocks.UBound) Then
      remoteHost = Winsocks(lngIndex).remoteHost
   End If
End Property
'
Public Property Get RemoteHostIP(ByVal lngIndex As Long) As String  ' Read only.
   If (lngIndex > 0) And (lngIndex <= Winsocks.UBound) Then
      RemoteHostIP = Winsocks(lngIndex).RemoteHostIP
   End If
End Property
'
Public Property Get remotePort(ByVal lngIndex As Long) As Long  ' Read only.
   If (lngIndex > 0) And (lngIndex <= Winsocks.UBound) Then
      remotePort = Winsocks(lngIndex).remotePort
   End If
End Property
'
' ------------------------------------------------------------------------------
' Methods.
' ------------------------------------------------------------------------------
'
Public Function StartListening() As Boolean
   '
   On Error GoTo StartError
   '
   ' Make sure we've been given a local port to listen on. Without this,
   ' the call to Listen() will fail.
   If (m_lngLocalPort) Then
      '
      ' If the socket is already listening, and it's listening on the same
      ' port, don't bother restarting it.
      If (Winsocks(0).State <> sckListening) Or _
                           (Winsocks(0).LocalPort <> m_lngLocalPort) Then
         With Winsocks(0)
            Call .Close
            .LocalPort = m_lngLocalPort
            Call .Listen
         End With
      End If
      ' Return true, since the server is now listening for clients.
      StartListening = True
      '
   End If
   addLog "tcp server startlinstening at " & Winsocks(0).LocalPort
   
   Exit Function
   '
StartError:
   ' Handle the error - either raise it or save the description.
   If (m_blnRaiseErrors) Then
      Call Err.Raise(Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Function
'
Public Function StopListening() As Boolean
   '
   ' Stop the listening socket so no more connection requests are received.
   Winsocks(0).Close
   StopListening = True
   '
End Function
'
Public Function Shutdown() As Boolean
   '
Dim i    As Long
   '
   On Error GoTo ShutdownError
   '
   ' Close the listening socket first, so no more connection requests.
   Call Winsocks(0).Close
   '
   ' Now loop through all the clients, close the active ones and
   ' unload them all to clear the array from memory.
   For i = 1 To Winsocks.UBound
      If (Winsocks(i).State <> sckClosed) Then Winsocks(i).Close
      Call Unload(Winsocks(i))
      RaiseEvent ConnectionClosed(i, Winsocks(i).RemoteHostIP)
   Next i
   '
   ' Return true if all went well.
   Shutdown = True
   Exit Function
   '
ShutdownError:
   ' Handle the error - either raise it or save the description.
   If (m_blnRaiseErrors) Then
      Call Err.Raise(Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Function
'
Public Function Send(ByVal lngIndex As Long, ByVal strData As String) As Boolean
   '
   On Error GoTo SendError
   '
   ' Send the data on the specified socket.
   Call Winsocks(lngIndex).SendData(strData): DoEvents
   '
   ' Return true if it went ok.
   Send = True
   Exit Function
   '
SendError:
   ' Handle the error - either raise it or save the description.
   If (m_blnRaiseErrors) Then
      Call Err.Raise(Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Function
'
Public Function SendToAll(ByVal strData As String) As Boolean
   '
Dim i    As Long
   '
   On Error GoTo SendToAllError
   '
   ' Loop through the control array of clients and send the data on each one.
   For i = 1 To Winsocks.UBound
      If (Winsocks(i).State = sckConnected) Then
         Call Winsocks(i).SendData(strData): DoEvents
      End If
   Next i
   '
   ' Return true if it all went well.
   SendToAll = True
   Exit Function
   '
SendToAllError:
   ' Handle the error - either raise it or save the description.
   If (m_blnRaiseErrors) Then
      Call Err.Raise(Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Function
'
Public Function CloseClient(ByVal lngIndex As Long) As Boolean
   '
   On Error GoTo CloseClientError
   '
   ' Make sure the index specified is within the range of the control array.
   If (lngIndex > 0) And (lngIndex <= Winsocks.UBound) Then
      '
      ' Close the socket, update the ActiveClients property, and raise a
      ' ConnectionClosed event to the parent.
      Call Winsocks(lngIndex).Close
      m_lngActiveClients = m_lngActiveClients - 1
      RaiseEvent ConnectionClosed(lngIndex, Winsocks(lngIndex).RemoteHostIP)
      '
   End If
   '
   ' Return true if all went well.
   CloseClient = True
   Exit Function
   '
CloseClientError:
   ' Handle the error - either raise it or save the description.
   If (m_blnRaiseErrors) Then
      Call Err.Raise(Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Function




'
' ------------------------------------------------------------------------------
' Winsock events.
' ------------------------------------------------------------------------------
'
Private Sub Winsocks_Close(Index As Integer)
   '
   ' Close the socket and raise the event to the parent.
   Call Winsocks(Index).Close
   addLog Winsocks(Index).RemoteHostIP & " close connection"
   RaiseEvent ConnectionClosed(Index, Winsocks(Index).RemoteHostIP)
   
   
   '
   ' If this wasn't the listening socket, reduce the ActiveClients property.
   If (Index > 0) Then
      m_lngActiveClients = m_lngActiveClients - 1
   End If
   '
End Sub
'
Private Sub Winsocks_ConnectionRequest(Index As Integer, ByVal requestID As Long)
   '
Dim i          As Long
Dim j          As Long
Dim blnLoaded  As Boolean
Dim blnCancel  As Boolean
   '
   On Error GoTo ConnectionRequestError
   '
   ' We shouldn't get ConnectionRequests on any other socket than the listener
   ' (index 0), but check anyway. Also check that we're not going to exceed
   ' the MaxClients property.
   If (Index = 0) And (m_lngActiveClients < m_lngMaxClients) Then
      '
      ' Check to see if we've got any sockets that are free.
      For i = 1 To Winsocks.UBound
         If (Winsocks(i).State = sckClosed) Then
            j = i
            Exit For
         End If
      Next i
      '
      ' If we don't have any free sockets, load another on the array.
      If (j = 0) Then
         blnLoaded = True
         Call Load(Winsocks(Winsocks.UBound + 1))
         j = Winsocks.Count - 1
      End If
      '
      ' With the selected socket, reset it and accept the new connection.
      With Winsocks(j)
         Call .Close
         Call .Accept(requestID)
      End With
      '
      ' Raise the NewConnection event, passing a Cancel boolean ByRef so
      ' the parent can cancel the connection if necessary.
      blnCancel = False
      RaiseEvent NewConnection(j, Winsocks(1).RemoteHostIP, blnCancel)
      addLog "new connection from " & Winsocks(1).RemoteHostIP
      
      '
      If (blnCancel) Then
         Call Winsocks(j).Close
         If (blnLoaded) Then Call Unload(Winsocks(j))
      Else
         m_lngActiveClients = m_lngActiveClients + 1
      End If
      '
   End If
   Exit Sub
   '
ConnectionRequestError:
   ' Close the Winsock that caused the error.
   Call Winsocks(0).Close
   '
   ' Handle the error - raise an error or store the description.
   If (m_blnRaiseErrors) Then
      RaiseEvent WinsockError(Index, Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Sub
'
Private Sub Winsocks_DataArrival(Index As Integer, ByVal bytesTotal As Long)
   '
Dim strData    As String
   '
   On Error GoTo DataArrivalError
   '
   ' Save the amount of data received.
   m_lngBytesReceived = m_lngBytesReceived + bytesTotal
   '
   ' Grab the data from the specified Winsock object, and pass it to the parent.
   Call Winsocks(Index).GetData(strData)
   
   addLog "tcp" & Index & " ip:" & Winsocks(Index).RemoteHostIP & " - " & strData
   
   RaiseEvent DataArrival(Index, Winsocks(Index).RemoteHostIP, strData)
   '
   Exit Sub
   '
DataArrivalError:
   ' Close the Winsock and update the ActiveClients property.
   Call Winsocks(Index).Close
   If (Index > 0) Then
      m_lngActiveClients = m_lngActiveClients - 1
   End If
   '
   ' Raise an error or store the description.
   If (m_blnRaiseErrors) Then
      RaiseEvent WinsockError(Index, Err.Number, Err.Source, Err.Description)
   Else
      m_strLastError = Err.Description
   End If
   '
End Sub
'
Private Sub Winsocks_Error(Index As Integer, ByVal Number As Integer, Description As String, ByVal Scode As Long, ByVal Source As String, ByVal HelpFile As String, ByVal HelpContext As Long, CancelDisplay As Boolean)
   '
   ' Close the Winsock and update the ActiveClients property.
   Call Winsocks(Index).Close
   If (Index > 0) Then
      m_lngActiveClients = m_lngActiveClients - 1
   End If
   '
   ' Raise an error or store the description.
   If (m_blnRaiseErrors) Then
      RaiseEvent WinsockError(Index, Number, Source, Description)
   Else
      m_strLastError = Description
   End If
   '
End Sub
'
Private Sub Winsocks_SendComplete(Index As Integer)
   '
   ' Pass the event on to the parent.
   RaiseEvent SendComplete(Index)
   '
End Sub
'
Private Sub Winsocks_SendProgress(Index As Integer, ByVal BytesSent As Long, ByVal bytesRemaining As Long)
   '
   ' Update the BytesSent property.
   m_lngBytesSent = m_lngBytesSent + BytesSent
   '
   ' Pass the event on to the parent.
   RaiseEvent SendProgress(Index, BytesSent, bytesRemaining)
   '
End Sub
'
' ------------------------------------------------------------------------------
' Usercontrol events.
' ------------------------------------------------------------------------------
'
Private Sub UserControl_Initialize()
   '
   'Call MsgBox("WinsockArray Usercontrol v2, www.WinsockVB.com", _
                                                vbOKOnly + vbInformation)
   '
End Sub

'
Private Sub UserControl_Terminate()
   '
   ' Shutdown the server (just in case the parent hasn't called it).
   Call Shutdown
   '
End Sub
'
' ------------------------------------------------------------------------------
' EOF.
' ------------------------------------------------------------------------------
'


Private Sub addLog(s As String)
    List1.AddItem Now & "  " & s
End Sub

Public Sub bindUDP(LocalPort As String)
    Set msgCol = New Collection
    Winsock1.Close
    
    udpLocalPort = CInt(LocalPort)
    Winsock1.bind udpLocalPort
    
    addLog "bindUDP at " & LocalPort
    
End Sub

Public Sub SendUDP(remoteAddr As String, remotePort As String, msg As String)
On Error GoTo hhh
    With Winsock1
     .remoteHost = remoteAddr
     .remotePort = CInt(remotePort)
      .SendData msg
    End With
    DoEvents

hhh:

End Sub

Private Sub Command1_Click()
    SendUDP txtIp.Text, txtPort.Text, Text1.Text
    
    
    
End Sub

Private Sub Command2_Click()
    bindUDP txtLocalPort.Text
End Sub

Private Sub Winsock1_DataArrival(ByVal bytesTotal As Long)
    Dim strData As String
    
    Winsock1.GetData strData, vbString
    strData = Winsock1.RemoteHostIP & "|" & strData
    List1.AddItem strData
    msgCol.Add strData
    RaiseEvent DataArrivalUDP
End Sub

Public Function peekMsg()
     If msgCol.Count = 0 Then
        peekMsg = "**nomore**"
    End If
    
    Dim s As String
    s = msgCol.Item(1)
    msgCol.Remove (1)
    peekMsg = s
End Function

Public Sub Broadcast(ByVal remoteHost As String, ByVal remotePort As String, ByVal Text As String)

 
  ' send!!
  WinsockBroadCast.remotePort = CLng(remotePort)
  
  WinsockBroadCast.remoteHost = remoteHost   '"255.255.255.255"
  
   
  WinsockBroadCast.SendData Text
  DoEvents

End Sub
Private Sub WinsockBroadCast_DataArrival(ByVal bytesTotal As Long)
    Dim strData As String
    WinsockBroadCast.GetData strData, vbString
    strData = "Broadcast from " & WinsockBroadCast.RemoteHostIP & "|" & strData
    List1.AddItem strData
    msgCol.Add strData
    RaiseEvent DataArrivalBroadCast
End Sub
Public Sub bindBroadCast(LocalPort As String)
    WinsockBroadCast.Close
    
    WinsockBroadCast.bind CInt(LocalPort)
    WinsockBroadCast.remotePort = CInt(LocalPort)
    
    addLog "bind broadcast at " & LocalPort
End Sub

Public Function tcpClientConnect(remoteIp As String, remotePort As String)
    tcpClient.Close
    
    tcpClient.remoteHost = remoteIp
    tcpClient.remotePort = CLng(remotePort)
    tcpClient.Connect
    
    Dim start As Date
    start = Now
    
    Do While tcpClient.State = sckConnecting Or DateDiff("s", start, Now) > 10
        DoEvents
    Loop
    
    tcpClientConnect = isConnected
    addLog "tcpclient connect to server " & remoteIp & ":" & remotePort
End Function

Public Function tcpClientSend(msg As String)
    If tcpClient.State = sckConnected Then
        tcpClient.SendData msg
        DoEvents

        tcpClientSend = True
    Else
        tcpClientSend = False
    End If
    
    
End Function

Public Function isConnected()
    If tcpClient.State = sckConnected Then
         isConnected = True
    Else
         isConnected = False
    End If
End Function

Private Sub tcpClient_DataArrival(ByVal bytesTotal As Long)
    Dim strData As String
    tcpClient.GetData strData, vbString
    addLog "client received from  " & tcpClient.RemoteHostIP & "|" & strData
    'msgCol.Add strData
    
    RaiseEvent DataArrivalClient(tcpClient.RemoteHostIP, strData)
    
End Sub
Private Sub tcpClient_Error(ByVal Number As Integer, Description As String, ByVal Scode As Long, ByVal Source As String, ByVal HelpFile As String, ByVal HelpContext As Long, CancelDisplay As Boolean)
    tcpClient.Tag = Number & "-" & Description
    
End Sub
Public Function tcpClientLastError()
    tcpClientLastError = tcpClient.Tag
End Function

Public Sub nap(l As Long)
    Sleep (l)
End Sub


