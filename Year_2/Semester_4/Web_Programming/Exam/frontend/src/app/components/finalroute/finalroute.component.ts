import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GeneralService } from 'src/app/core/service/general.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-finalroute',
  templateUrl: './finalroute.component.html',
  styleUrls: ['./finalroute.component.css'],
})
export class FinalrouteComponent implements OnInit {
  route: any = [];

  constructor(
    private router: Router,
    private toastrService: ToastrService,
    private generalService: GeneralService,
    private activatedRoute: ActivatedRoute,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.route = this.storageService.getRoute();
    if (!this.route.hasOwnProperty('route')) {
      this.route = { route: [] };
    }
    this.storageService.setRoute(this.route);
  }
}
