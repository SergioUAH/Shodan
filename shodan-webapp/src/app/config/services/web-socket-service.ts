import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';

@Injectable()
export class WebSocketService {
  endPointController: string = '/topic/connectionLog';
  endPointDestination: string = '/app/logger';
  endPointWS: string = 'ws://localhost:8085/SHD-APP/logging';
  // endPointFallBack: string = 'http://localhost:8085/SHD-APP/logging';
  client: Client;
  isWSConnected: boolean = false;

  setupConnection(callbackFunction) {
    const old_this = this;
    this.client = new Client({
      brokerURL: this.endPointWS,
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });
    // if (typeof WebSocket !== 'function') {
    //   // For SockJS you need to set a factory that creates a new SockJS instance
    //   // to be used for each (re)connect
    //   this.client.webSocketFactory = function () {
    //     // Note that the URL is different from the WebSocket URL
    //     return new SockJS(old_this.endPointFallBack);
    //   };
    // }
    this.client.onConnect = function (frame) {
      old_this.isWSConnected = true;
      old_this.checkResponse(callbackFunction);
    }
    this.client.onStompError = function (frame) {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };
  }

  isConnected() {
    return this.isWSConnected;
  }

  openConnection(callbackFunction) {
    if (!this.client || !this.client.connected) {
      this.setupConnection(callbackFunction);
      this.client.activate();
    }
  }

  closeConnection() {
    if (this.client.connected) {
      this.client.deactivate();
    }
  }

  checkResponse(callbackFunction) {
    if (this.client.connected) {
      this.client.subscribe(this.endPointController, callbackFunction);
    }
  }

  sendMessage(content) {
      let param = {
        text: content
      };
      this.client.publish({ destination: this.endPointDestination, body: JSON.stringify(param) });
  }

}
