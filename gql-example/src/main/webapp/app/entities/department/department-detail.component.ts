import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Department } from './department.model';
import { DepartmentService } from './department.service';

@Component({
    selector: 'jhi-department-detail',
    templateUrl: './department-detail.component.html'
})
export class DepartmentDetailComponent implements OnInit, OnDestroy {

    department: Department;
    private subscription: any;

    constructor(
        private departmentService: DepartmentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.departmentService.find(id).subscribe(department => {
            this.department = department;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
