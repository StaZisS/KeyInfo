import { ApproveButton } from "./approveButton"
import { Approved } from "./approved"
import { DisapproveButton } from "./disapproveButton"

import('../../../../styles/applicationItem.css')

export const ApplicationItem = () => {
    return (
        <div className='w-100 d-flex item border flex-wrap'>
        
            <div className='d-flex h-100 align-items-center ' >   
                <div className='ps-3 '>
                    <span className='key-number'>221</span>
                </div>
                <div className=''>
                    <span className='location me-3'>студент Петров</span>
                </div>
                
            </div>
            
            <div className="buttons d-flex h-100 align-items-center">
            <span className="repeat me-5">Повторяющаяся</span>
                <Approved/>
                <ApproveButton/>
                <DisapproveButton/>
                </div>

        </div>
    )}