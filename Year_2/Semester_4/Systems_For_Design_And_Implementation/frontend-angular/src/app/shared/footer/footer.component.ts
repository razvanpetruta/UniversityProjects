import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  currentDateTime?: Date;
  userLocale?: string;

  ngOnInit(): void {
    this.currentDateTime = new Date();
    this.userLocale = navigator.language;
    setInterval(() => {
      this.currentDateTime = new Date();
    }, 1000);
  }
}
