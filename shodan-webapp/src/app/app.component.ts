import { Component } from '@angular/core';
import { faGithub, faGithubSquare } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'shodan-webapp';
  faGithub = faGithubSquare;
}
