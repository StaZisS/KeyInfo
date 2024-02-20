import('../../../../styles/peopleItem.css')

export const ButtonStudent = ({roles,callback}) => {
    return(
        <button onClick={callback} className={`${roles.includes('STUDENT') ? 'button-role' : 'button-role'} py-2 px-4`}>
            <span className='buttonName'>Студент</span>
        </button>
    )
}