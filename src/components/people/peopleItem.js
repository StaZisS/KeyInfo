import { ButtonStudent } from './studentRoleButton'
import { ButtonTeacher } from './teacherRoleButton'

import('../../styles/peopleItem.css')

export const PeopleItem = ({name}) => {
    return (
        <div className='w-100 d-flex item border flex-wrap'>
        
            <div className='d-flex flex-grow-1 h-100 align-items-center'>
                <div className='ps-3 d-flex align-items-center flex-grow-1 number'>
                    <span className='name'>{name}</span>
                </div>
            </div>
            <div className='h-100 d-flex align-items-center'>
            <ButtonStudent/>
            <ButtonTeacher/>
            </div>

            
        </div>
    )
}