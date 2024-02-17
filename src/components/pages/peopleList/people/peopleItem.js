import {ButtonStudent} from './studentRoleButton'
import {ButtonTeacher} from './teacherRoleButton'

import('../../../../styles/peopleItem.css')

export const PeopleItem = ({name}) => {
    return (
        <div className='w-100 d-flex item border flex-nowrap justify-content-between'>

            <div className='d-flex h-100 align-items-center'>
                <div className='ps-3 d-flex align-items-center flex-grow-0 number'>
                    <span className='name'>{name}</span>
                </div>

            </div>
            <div className="buttons d-flex h-100 align-items-center">
                <ButtonStudent/>
                <ButtonTeacher/>
            </div>

        </div>
    )
}