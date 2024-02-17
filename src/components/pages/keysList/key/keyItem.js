import { ButtonDel } from './buttonDel'
import { ButtonGet } from './buttonGet'
import { Mark } from './mark'
import('../../../../styles/keysItem.css')


export const KeysItem = ({ keyNumber, hasRequests, location }) => {
    // const history = useNavigate();

    // const handleClick = () => {
    //     if (hasRequests) {
    //         history.push('/key/application');
    //     }
    // };

    return (
        <div className='w-100 d-flex item border flex-wrap'>
        
            <div className={`d-flex flex-grow-1 h-100 align-items-center  ${hasRequests ? 'has-requests' : ''}`} >     {/*onClick={handleClick}*/}
                <div className='ps-3 d-flex align-items-center flex-grow-1 number'>
                    <span className='key-number'>Аудитория {keyNumber}</span>
                    {hasRequests && <Mark />}
                </div>
                <div className='flex-grow-1'>
                    <span className='location'>{location}</span>
                </div>
            </div>

            {location === 'в деканате' ? <ButtonDel /> : <ButtonGet />}
        </div>
    )
}