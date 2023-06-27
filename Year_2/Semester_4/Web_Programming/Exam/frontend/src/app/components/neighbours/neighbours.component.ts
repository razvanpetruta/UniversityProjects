import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GeneralService } from 'src/app/core/service/general.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-neighbours',
  templateUrl: './neighbours.component.html',
  styleUrls: ['./neighbours.component.css'],
})
export class NeighboursComponent implements OnInit {
  cityID?: string;
  city?: any = undefined;
  neighbours: any = [];
  route: any = [];

  constructor(
    private router: Router,
    private toastrService: ToastrService,
    private generalService: GeneralService,
    private activatedRoute: ActivatedRoute,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params) => {
      this.cityID = params['id'];

      this.generalService.getCityById(this.cityID!).subscribe({
        next: (res) => {
          this.city = res.value;
          this.route = this.storageService.getRoute();
          if (!this.route.hasOwnProperty('route')) {
            this.route = { 'route': [] };
          }
          this.route.route.push(this.city);
          this.storageService.setRoute(this.route);
        },
        error: (error) => {
          console.log(error);
        },
      });

      this.generalService.getNeighbours(this.cityID!).subscribe({
        next: (res) => {
          this.neighbours = res.value;
          this.neighbours.sort(
            (a: any, b: any) =>
              (0.6 * a.duration + 0.4 * a.distance) 
              -
              (0.6 * b.duration + 0.4 * b.distance)
          );
        },
        error: (error) => {
          console.log(error);
        },
      });
    });
  }

  goBack(): void {
    if (this.route.route.length > 1) {
      this.route.route.pop();
      const city: any = this.route.route.at(-1);
      this.route.route.pop();
      this.storageService.setRoute(this.route);
      if (this.route.route.length + 1 > 0) {
        this.router.navigateByUrl("/neighbours/" + city.id);
      } else {
        this.router.navigateByUrl("/dashboard");
      }
    } else {
      this.router.navigateByUrl("/dashboard");
    }

  }
}
