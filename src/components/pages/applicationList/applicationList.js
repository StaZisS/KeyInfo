import { Day } from "./application/day";

const keysData = [
    { keyNumber: 111, hasRequests: true, location: 'в деканате' },
    { keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-nj' },
    { keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-njsrtrtettr' },
    { keyNumber: 333, hasRequests: true, location: 'в деканате' }
];
export const Application = () => {
    return (
        <div className="col-10 col-md-8 mx-auto mt-3 bg-white p-4 shadow border-0">
            <div className="d-flex align-items-end"> 
                <h2 className="my-0 me-1">221</h2>
                <span>в деканате</span>
            </div>
            <Day/>
            <Day/>
            
            
        </div>
    )
}