import { Component, OnInit } from '@angular/core';
import { trigger, state, transition, animate, style } from '@angular/animations';


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css'],
  animations: [
    trigger('fadein', [
      state('initial', style({
        opacity: 0
      })),
      state('final', style({
        opacity: 1
      })),
      transition('initial => final', [
        animate('1s', style({
          opacity: 1
        }))
      ])
    ]),
  ]
})
export class MainPageComponent implements OnInit {
  fadeState: string = 'initial';

  ngOnInit(): void {
    this.fadeState = 'initial';
    setTimeout(() => this.fadeState = 'final', 100);
  }
}
