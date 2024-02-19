import { ButtonDel } from './buttonDel'
import { ButtonGet } from './buttonGet'
import { Mark } from './mark'
import('../../../../styles/keysItem.css')


export const KeysItem = ({build, room, hasRequests, location,owner }) => {

    return (
        <div className='w-100 d-flex item border flex-wrap'>
        
            <div className={`d-flex flex-grow-1 h-100 align-items-center  ${hasRequests ? 'has-requests' : ''}`} >
                <div className='ps-3 d-flex align-items-center gap-3 flex-grow-1 number'>
                    <span className='key-number'>Здание <span className='fw-bold'>{build}</span></span>
                    <span className='key-number'>Аудитория <span className='fw-bold'>{room}</span></span>
                    {hasRequests && <Mark />}
                </div>
                <div className='flex-grow-1 gap-3'>
                    <span className='location'>{location === 'IN_DEANERY' && 'В деканате' }</span>
                    <span className='location fw-bolder'>{owner && owner.name}</span>
                </div>
            </div>

            {location === 'IN_DEANERY'? <ButtonGet /> : <ButtonDel />}
        </div>
    )
}