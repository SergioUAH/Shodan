import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable()
export class WebSocketService {
  endPointController: string = '/logger/connectionLog';
  client: any;
  endPointWS: string = 'http://localhost:8085/log'

  openConnection() {
    let webSocket = new SockJS(this.endPointWS);
    this.client = Stomp.over(webSocket);
    const aux = this;
    aux.client.connect({}, function (frame) {
      aux.client.subscribe(aux.endPointController, function (stompEvent) {
        aux.listen(stompEvent);
      });
    });
  }

  closeConnection() {
    if (this.client !== null) {
      this.client.disconnect();
  }
  console.log("Disconnected");
  }

 listen(stompEvent: any) {
    throw new Error("Method not implemented.");
  }

  }
