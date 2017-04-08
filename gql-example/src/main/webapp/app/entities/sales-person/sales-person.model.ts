export class SalesPerson {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public hireDate?: any,
        public salary?: number,
        public commissionPct?: number,
        public departmentId?: number,
        public salesPersonId?: number,
    ) {
    }
}
