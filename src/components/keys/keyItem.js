import { ButtonDel } from './buttonDel'
import { ButtonGet } from './buttonGet'
import { Mark } from './mark'
import('../../styles/keysItem.css')

export const KeysItem = () => {
    return(
        <div className="w-100 border d-flex item" border="secondary">
        <div className='ps-3 d-flex align-items-center flex-grow-1'>
            <span className='number pe-4'>111</span>
            <Mark/>
        </div>
        <div className='flex-grow-1'>
            <span className='location'>в деканате</span>
        </div>
            <ButtonGet/>
        </div>
    )

}