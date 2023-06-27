import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { GeneralService } from 'src/app/core/service/general.service';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  allCities: any = [];

  constructor(private generalService: GeneralService,
    private toastrService: ToastrService,
    private storageService: StorageService) {}

  ngOnInit(): void {
    this.getAllCities();

    this.storageService.setRoute({ route: [] });
  }

  getAllCities(): void {
    this.generalService.getAllCities().subscribe({
      next: (res) => {
        this.allCities = res.value;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}
